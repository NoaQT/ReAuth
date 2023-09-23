package technicianlp.reauth.mixin;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import technicianlp.reauth.gui.Handler;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer {

	@Inject(method = "initGui", at = @At("TAIL") )
	public void onInitGui(CallbackInfo ci) {
		Handler.openGuiMultiplayer((GuiMultiplayer) (Object) this);
	}

	@Inject(method = "drawScreen", at = @At("TAIL") )
	public void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		Handler.onGuiMultiplayerDrawScreen((GuiMultiplayer) (Object) this);
	}

	@Inject(method = "actionPerformed", at = @At("TAIL") )
	public void onActionPerformed(GuiButton button, CallbackInfo ci) {
		Handler.onActionPerformed(button.id);
	}

//	@Inject(method = "actionPerformed", at = @At("HEAD") )
//	public void onPreActionPerformed(GuiButton button, CallbackInfo ci) {
//		GuiHandler.preGuiMultiplayerActionPerformed(button.id);
//	}

}
