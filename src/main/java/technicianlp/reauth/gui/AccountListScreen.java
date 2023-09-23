package technicianlp.reauth.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import technicianlp.reauth.LiteModReAuth;
import technicianlp.reauth.authentication.YggdrasilAPI;
import technicianlp.reauth.authentication.flows.Flows;
import technicianlp.reauth.configuration.Config;
import technicianlp.reauth.configuration.Profile;
import technicianlp.reauth.configuration.ProfileConstants;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class AccountListScreen extends GuiScreen {

	private GuiScreen parentScreen;

	private GuiButton loginButton;
	private GuiButton cancelButton;
	private GuiButton addButton;
	private GuiButton removeButton;

	private Profile selectedProfile;
	private GuiSlotAccounts accountList;

	private final Config config;

	public AccountListScreen(GuiScreen parentScreen) {
		this.config = LiteModReAuth.config;
		this.parentScreen = parentScreen;
	}

	@Override
	public void initGui() {
		super.initGui();

		addButton(loginButton = new GuiButton(0, 10, height - 50, width / 2 - 30, 20, I18n.format("reauth.gui.button.login")));
		addButton(cancelButton = new GuiButton(1, width / 2 + 20, height - 50, width / 2 - 30, 20, I18n.format("gui.cancel")));
		addButton(addButton = new GuiButton(2, 10, height - 25, width / 2 - 30, 20, I18n.format("reauth.gui.button.addaccount")));
		addButton(removeButton = new GuiButton(4, width / 2 + 20, height - 25, width / 2 - 30, 20, I18n.format("reauth.gui.button.removeaccount")));

		if (config.getProfiles().isEmpty()) {
			loginButton.enabled = false;
			removeButton.enabled = false;
		} else {
			selectedProfile = config.getProfile();
		}

		accountList = new GuiSlotAccounts(mc, width, height, 50, height - 60, 38);

		YggdrasilAPI.initSkinStuff();
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		accountList.handleMouseInput();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		accountList.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);

		drawCenteredString(fontRenderer, I18n.format("reauth.gui.ttile.accountlist"), width / 2, 10, 0xffffff);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			FlowScreen.open(Flows::loginWithProfile, selectedProfile, this.parentScreen);
			break;
		case 1:
			mc.displayGuiScreen(parentScreen);
			break;
		case 2:
			mc.displayGuiScreen(new LoginScreen(mc.currentScreen));
			break;
		case 4:
			config.getProfiles().remove(selectedProfile);
			LiteModReAuth.config.save();
			break;
		}
	}

	private class GuiSlotAccounts extends GuiSlot {
		public GuiSlotAccounts(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
			super(mcIn, width, height, topIn, bottomIn, slotHeightIn);
		}

		@Override
		protected int getSize() {
			return config.getProfiles().size();
		}

		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
			selectedProfile = config.getProfile(slotIndex);
			if (isDoubleClick) {
				AccountListScreen.this.actionPerformed(loginButton);
			}
		}

		@Override
		protected boolean isSelected(int slotIndex) {
			if (selectedProfile == null) return false;
			return selectedProfile.equals(config.getProfile(slotIndex));
		}

		@Override
		protected void drawBackground() {
			drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
			Profile profile = config.getProfile(slotIndex);
			String username = profile.getValue(ProfileConstants.NAME);

			drawString(fontRenderer, username, xPos + 50, yPos + 10, 0xffffff);

			GameProfile gameProfile = new GameProfile(null, username);
			gameProfile = TileEntitySkull.updateGameprofile(gameProfile);
			Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> profileTextures = Minecraft.getMinecraft()
					.getSkinManager().loadSkinFromCache(gameProfile);
			ResourceLocation skinLocation;
			if (profileTextures.containsKey(MinecraftProfileTexture.Type.SKIN)) {
				skinLocation = Minecraft.getMinecraft().getSkinManager().loadSkin(
						profileTextures.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
			} else {
				UUID id = EntityPlayer.getUUID(gameProfile);
				skinLocation = DefaultPlayerSkin.getDefaultSkin(id);
			}

			Minecraft.getMinecraft().getTextureManager().bindTexture(skinLocation);
			drawScaledCustomSizeModalRect(xPos + 1, yPos + 1, 8, 8, 8, 8, 32, 32, 64, 64);
		}

	}

}
