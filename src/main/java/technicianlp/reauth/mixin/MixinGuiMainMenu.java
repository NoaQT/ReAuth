package technicianlp.reauth.mixin;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import technicianlp.reauth.gui.Handler;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {

	@Inject(method = "initGui", at = @At("TAIL") )
	public void onInitGui(CallbackInfo ci) {
		Handler.openGuiMainMenu((GuiMainMenu) (Object) this);
	}

	@Inject(method = "actionPerformed", at = @At("TAIL") )
	public void onActionPerformed(GuiButton button, CallbackInfo ci) {
		Handler.onActionPerformed(button.id);
	}

}
