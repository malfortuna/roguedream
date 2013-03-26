package net.cutgar.ard.ui;

import net.cutgar.ard.state.PlayState;

import org.flixel.event.IFlxButton;

public class ClearTooltip implements IFlxButton {

	@Override
	public void callback() {
		PlayState.tooltip.setText("");
	}

}
