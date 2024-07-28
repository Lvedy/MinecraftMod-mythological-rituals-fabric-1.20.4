package lvedy.mr.special.ItemType;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommonBlockItem extends BlockItem {
    protected String ItemIntroduce = "default";
    protected Formatting color = Formatting.GRAY;
    public CommonBlockItem(Block block, Settings settings,String introduce,Formatting formatting) {
        super(block, settings);
        ItemIntroduce = introduce;
        color = formatting;
    }
    public CommonBlockItem(Block block, Settings settings) {
        super(block, settings);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (!ItemIntroduce.equals("default")) {
            tooltip.add(Text.translatable(ItemIntroduce).formatted(color));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
