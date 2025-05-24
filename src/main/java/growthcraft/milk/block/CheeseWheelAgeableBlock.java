package growthcraft.milk.block;

import net.minecraft.world.level.block.state.BlockState;

public class CheeseWheelAgeableBlock extends BaseCheeseWheel {
    public CheeseWheelAgeableBlock(Cheese variant) {
        super(variant);
    }
    
	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return true;
	}
}
