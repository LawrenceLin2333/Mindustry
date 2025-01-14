package mindustry.world.blocks.logic;

import arc.scene.ui.layout.Table;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.ui.Styles;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class MemoryBlock extends Block{
    public int memoryCapacity = 32;

    public MemoryBlock(String name){
        super(name);
        destructible = true;
        solid = true;
        configurable = true;
        group = BlockGroup.logic;
        drawDisabled = false;
        envEnabled = Env.any;
    }

    @Override
    public void setStats(){
        super.setStats();

        stats.add(Stat.memoryCapacity, memoryCapacity, StatUnit.none);
    }

    public class MemoryBuild extends Building{
        public double[] memory = new double[memoryCapacity];

        //massive byte size means picking up causes sync issues
        @Override
        public boolean canPickup(){
            return false;
        }

        @Override
        public void buildConfiguration(Table table){
            table.button(Icon.eyeSmall, Styles.clearTransi, () -> {
                ui.memory.setMemory(memory);
                ui.memory.toggle();
            }).size(40);
        }

        @Override
        public void write(Writes write){
            super.write(write);

            write.i(memory.length);
            for(double v : memory){
                write.d(v);
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);

            int amount = read.i();
            for(int i = 0; i < amount; i++){
                double val = read.d();
                if(i < memory.length) memory[i] = val;
            }
        }
    }
}
