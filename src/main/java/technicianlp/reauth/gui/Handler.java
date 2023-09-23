package technicianlp.reauth.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Session;
import technicianlp.reauth.session.SessionChecker;
import technicianlp.reauth.session.SessionStatus;

public final class Handler {
    public static void openGuiMultiplayer(GuiMultiplayer gui) {
        ((IGuiScreen) gui).doAddButton(new GuiButton(17325, 5, 5, 100, 20, I18n.format("reauth.gui.button")));
    }
    
    public static void openGuiMainMenu(GuiMainMenu gui) {
    	// Support for Custom Main Menu (add button outside of viewport)
        ((IGuiScreen) gui).doAddButton(new GuiButton(17325, -50, -50, 20, 20, I18n.format("reauth.gui.title.main")));
    }

    public static void onGuiMultiplayerDrawScreen(GuiMultiplayer gui) {
        Session user = Minecraft.getMinecraft().getSession();
        SessionStatus state = SessionChecker.getSessionStatus(user.getToken(), user.getPlayerID());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(I18n.format(state.getTranslationKey()), 110, 10, 0xFFFFFFFF);
    }

    public static void onActionPerformed(int buttonId) {
        if (buttonId == 17325) {
            Minecraft.getMinecraft().displayGuiScreen(new AccountListScreen(Minecraft.getMinecraft().currentScreen));
        }
    }
}
