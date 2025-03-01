package mindustry.world.blocks;

import arc.func.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class ItemSelection{

    public static <T extends UnlockableContent> void buildTable(Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer){
        buildTable(table, items, holder, consumer, true);
    }

    public static <T extends UnlockableContent> void buildTable(Block block, Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer){
        buildTable(block, table, items, holder, consumer, true, 4);
    }

    public static <T extends UnlockableContent> void buildTable(Block block, Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer, int column){
        buildTable(block, table, items, holder, consumer, true, column);
    }

    public static <T extends UnlockableContent> void buildTable(Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer, boolean closeSelect){
        buildTable(null, table, items, holder, consumer, closeSelect, 4);
    }

    public static <T extends UnlockableContent> void buildTable(@Nullable Block block, Table table, Seq<T> items, Prov<T> holder, Cons<T> consumer, boolean closeSelect, int column){

        ButtonGroup<ImageButton> group = new ButtonGroup<>();
        group.setMinCheckCount(0);
        Table cont = new Table();
        cont.defaults().size(40);

        int i = 0;

        for(T item : items){
            if(!item.unlockedNow()) continue;

            ImageButton button = cont.button(Tex.whiteui, Styles.clearToggleTransi, 24, () -> {
                if(closeSelect) control.input.frag.config.hideConfig();
            }).group(group).get();
            button.changed(() -> consumer.get(button.isChecked() ? item : null));
            button.getStyle().imageUp = new TextureRegionDrawable(item.uiIcon);
            button.update(() -> button.setChecked(holder.get() == item));

            if(i++ % column == column - 1){
                cont.row();
            }
        }

        //add extra blank spaces so it looks nice
        if(i % column != 0){
            int remaining = column - (i % column);
            for(int j = 0; j < remaining; j++){
                cont.image(Styles.black6);
            }
        }

        ScrollPane pane = new ScrollPane(cont, Styles.smallPane);
        pane.setScrollingDisabled(true, false);

        if(block != null){
            pane.setScrollYForce(block.selectScroll);
            pane.update(() -> {
                block.selectScroll = pane.getScrollY();
            });
        }

        pane.setOverscroll(false, false);
        table.add(pane).maxHeight(Scl.scl(40 * 8));
    }
}
