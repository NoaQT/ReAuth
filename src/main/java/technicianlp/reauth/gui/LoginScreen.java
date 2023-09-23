package technicianlp.reauth.gui;

import com.mumfrey.liteloader.client.gui.GuiCheckbox;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import technicianlp.reauth.authentication.flows.Flows;

import java.awt.*;
import java.io.IOException;

final class LoginScreen extends AbstractScreen {
    private GuiButton loginAuthCode;
    private GuiButton loginDeviceCode;
    private GuiButton offline;
    private GuiCheckbox save;

    private int basey;

    private String message = "";

    LoginScreen(GuiScreen background) {super("reauth.gui.title.main", background);}

    @Override
    protected void actionPerformed(GuiButton b) throws IOException {
        super.actionPerformed(b);
        switch (b.id) {
            case 0: // Save checked
                this.save.checked = !this.save.checked;
                break;
            case 2: // LoginAuthCode
                FlowScreen.open(Flows::loginWithAuthCode, this.save.checked, this.background);
                break;
            case 3: // LoginDeviceCode
                FlowScreen.open(Flows::loginWithDeviceCode, this.save.checked, this.background);
                break;
            case 4: // Offline
                this.mc.displayGuiScreen(new OfflineLoginScreen(this.background));
                break;
        }

    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

        this.drawString(this.fontRenderer, I18n.format("reauth.gui.text.microsoft"), this.centerX - 105, this.basey, Color.WHITE.getRGB());
        this.drawString(this.fontRenderer, I18n.format("reauth.gui.text.offline"), this.centerX - 105, this.basey + 63, Color.WHITE.getRGB());

//        if (!(this.message == null || this.message.isEmpty())) {
//            this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, this.basey - 15, 0xFFFFFF);
//        }

    }

    @Override
    public void initGui() {
        super.initGui();

        this.basey = this.centerY - 40;

        this.loginAuthCode = new GuiButton(2, this.centerX - 105, this.basey + 12, 100, 20, I18n.format("reauth.gui.button.authcode"));
        this.buttonList.add(this.loginAuthCode);
        this.loginDeviceCode = new GuiButton(3, this.centerX + 5, this.basey + 12, 100, 20, I18n.format("reauth.gui.button.devicecode"));
        this.buttonList.add(this.loginDeviceCode);

        this.save = new GuiCheckbox(0, this.centerX - 105, this.basey + 35, I18n.format("reauth.gui.button.save"));
        this.buttonList.add(this.save);

        this.offline = new GuiButton(4, this.centerX - 105, this.basey + 75, 210, 20, I18n.format("reauth.gui.button.offline"));
        this.buttonList.add(this.offline);
    }

    /**
     * used as an interface between this and the secure class
     * <p>
     * returns whether the login was successful
     */
    private boolean login() {
//        try {
//            Secure.login("this.username.getText(", "this.pw.getPW()".toCharArray(), this.save.checked);
//            this.message = (char) 167 + "aLogin successful!";
//            return true;
//        } catch (AuthenticationException e) {
//            this.message = (char) 167 + "4Login failed: " + e.getMessage();
//            LiteModReAuth.log.error("Login failed:", e);
//            return false;
//        } catch (Exception e) {
//            this.message = (char) 167 + "4Error: Something went wrong!";
//            LiteModReAuth.log.error("Error:", e);
//            return false;
//        }
        return false;
    }
}
