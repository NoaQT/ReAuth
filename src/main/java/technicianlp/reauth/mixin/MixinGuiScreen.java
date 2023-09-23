package technicianlp.reauth.mixin;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import technicianlp.reauth.gui.IGuiScreen;

import java.net.URI;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen implements IGuiScreen {
	@Shadow
	protected abstract <T extends GuiButton> T addButton(T button);

	@Override
	public <T extends GuiButton> T doAddButton(T button) {
		return addButton(button);
	}

	@Shadow
	private void openWebLink(URI url) {
		openWebLink(url);
	}

	@Override
	public void doOpenWebLink(URI url) {
		openWebLink(url);
	}
}
