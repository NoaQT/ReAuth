package technicianlp.reauth.gui;

import net.minecraft.client.gui.GuiButton;

import java.net.URI;

public interface IGuiScreen {

	<T extends GuiButton> T doAddButton(T button);

	void doOpenWebLink(URI url);
}
