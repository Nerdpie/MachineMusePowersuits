package net.machinemuse.general.gui.frame;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.numina.geometry.MusePoint2D;
import net.machinemuse.numina.geometry.MuseRelativeRect;
import net.machinemuse.powersuits.client.modelspec.ModelRegistry;
import net.machinemuse.powersuits.client.modelspec.Spec;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:39 PM, 29/04/13
 *
 * Ported to Java by lehjr on 11/9/16.
 */
@SideOnly(Side.CLIENT)
public class PartSpecManipContainer extends ScrollableFrame {
    public ItemSelectionFrame itemSelect;
    public ColourPickerFrame colourSelect;
    public MusePoint2D topleft;
    public MusePoint2D bottomright;
    public Integer lastItemSlot;
    public int lastColour;
    public int lastColourIndex;
    public List<PartSpecManipSubFrame> modelframes;

    public PartSpecManipContainer(ItemSelectionFrame itemSelect,
                                  ColourPickerFrame colourSelect,
                                  MusePoint2D topleft,
                                  MusePoint2D bottomright,
                                  Colour borderColour,
                                  Colour insideColour) {
        super(topleft, bottomright, borderColour, insideColour);

        this.itemSelect = itemSelect;
        this.colourSelect = colourSelect;
        this.topleft = topleft;
        this.bottomright = bottomright;
        this.lastItemSlot = null;
        this.lastColour = this.getColour();
        this.lastColourIndex = this.getColourIndex();
        this.modelframes = getModelframes();
    }

    public ItemStack getItem() {
        return (itemSelect.getSelectedItem() != null) ? itemSelect.getSelectedItem().getItem() : new ItemStack(Blocks.AIR);
    }

    @Nullable
    public Integer getItemSlot() {
        return (itemSelect.getSelectedItem() != null)  ? itemSelect.getSelectedItem().inventorySlot : null;
    }

    public int getColour() {
        if (getItem().isEmpty())
            return Colour.WHITE.getInt();
        if(colourSelect.selectedColour < colourSelect.colours().length && colourSelect.selectedColour >= 0)
            return colourSelect.colours()[colourSelect.selectedColour];
        else
            return Colour.WHITE.getInt();
    }

    public int getColourIndex() {
        return this.colourSelect.selectedColour;
    }

    public List<PartSpecManipSubFrame> getModelframes() {
        List<PartSpecManipSubFrame> modelframesList = new ArrayList<>();
        Collection<Spec> specCollection = ModelRegistry.getInstance().apply().values();

        PartSpecManipSubFrame prev = null;
        PartSpecManipSubFrame newframe;
        for (Spec modelspec : specCollection) {
            System.out.println("Spec: " + modelspec.getDisaplayName());

            newframe = createNewFrame(modelspec, prev);
            prev = newframe;
            modelframesList.add(newframe);
        }
        return modelframesList;
    }

    public PartSpecManipSubFrame createNewFrame(Spec modelspec, PartSpecManipSubFrame prev) {
        MuseRelativeRect newborder = new MuseRelativeRect(this.topleft.x() + 4, this.topleft.y() + 4, this.bottomright.x(), this.topleft.y() + 10);
        newborder.setBelow((prev!= null) ? prev.border : null);
        return new PartSpecManipSubFrame(modelspec, this.colourSelect, this.itemSelect, newborder);
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        if (button == 0) {
            for (PartSpecManipSubFrame frame : modelframes) {
                frame.tryMouseClick(x, y + currentscrollpixels);
            }
        }
    }

    @Override
    public void update(double mousex, double mousey) {
        super.update(mousex, mousey);
        if (!Objects.equals(lastItemSlot, getItemSlot())) {
            lastItemSlot = getItemSlot();

            double x = 0;
            for (PartSpecManipSubFrame subframe : modelframes) {
                subframe.updateItems();
                x += subframe.border.bottom();
            }
            this.totalsize = (int) x;
        }
        if(colourSelect.decrAbove > -1) {
            decrAbove(colourSelect.decrAbove);
            colourSelect.decrAbove = -1;
        }
    }

    public void decrAbove(int index) {
        for(PartSpecManipSubFrame frame : modelframes) frame.decrAbove(index);
    }

    @Override
    public void draw() {
        super.preDraw();
        GL11.glPushMatrix();
        GL11.glTranslated(0.0, (double)(-this.currentscrollpixels), 0.0);
        for (PartSpecManipSubFrame f : modelframes) {
            f.drawPartial(currentscrollpixels + 4 + border.top(), this.currentscrollpixels + border.bottom() - 4);
        }
        GL11.glPopMatrix();
        super.postDraw();
    }
}