package MobLimiter;

import PluginReference.MC_Entity;
import PluginReference.MC_EntityType;
import PluginReference.MC_Location;
import PluginReference.MC_Server;
import PluginReference.MC_World;
import PluginReference.PluginBase;
import gyurix.konfigfajl.ConfigFile;
import gyurix.konfigfajl.KFA;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MyPlugin extends PluginBase
{
  HashMap<String, Integer> blockCount = new HashMap();
  HashMap<String, Integer> chunkCount = new HashMap();
  ConfigFile kf;
  int blocklimit;
  int chunklimit;
  int intervall;

  public void onServerFullyLoaded()
  {
    String dir = KFA.fileCopy(this, "config.yml", false);
    this.kf = new ConfigFile(dir + "/config.yml");
    this.blocklimit = ((int)this.kf.getLong("per-block-limit", 4L));
    this.chunklimit = ((int)this.kf.getLong("per-chunk-limit", 50L));
    this.intervall = ((int)this.kf.getLong("check-intervall", 1000L));
  }

  public void onTick(int tickNumber) {
    if (tickNumber % this.intervall == 0)
    {
      Iterator localIterator2;
      for (Iterator localIterator1 = KFA.srv.getWorlds().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
      {
        MC_World w = (MC_World)localIterator1.next();
        this.blockCount.clear();
        this.chunkCount.clear();
        localIterator2 = w.getEntities().iterator(); continue; MC_Entity e = (MC_Entity)localIterator2.next();
        String adr = e.getLocation().getBlockX() + " " + e.getLocation().getBlockY() + " " + e.getLocation().getBlockZ();
        if (this.blockCount.containsKey(adr)) {
          Integer num = (Integer)this.blockCount.get(adr);
          if (num.intValue() == this.blocklimit) {
            System.out.println("[MobLimiter] Removed a " + e.getType().name() + " for block entity limitation.");
            e.removeEntity();
          }
          else {
            this.blockCount.put(adr, Integer.valueOf(num.intValue() + 1));
          }
        }
        else {
          this.blockCount.put(adr, Integer.valueOf(1));
        }
        adr = e.getLocation().getBlockX() / 16 + " " + e.getLocation().getBlockZ() / 16;
        if (this.chunkCount.containsKey(adr)) {
          Integer num = (Integer)this.chunkCount.get(adr);
          if (num.intValue() == this.chunklimit) {
            System.out.println("[MobLimiter] Removed a " + e.getType().name() + " for chunk entity limitation.");
            e.removeEntity();
          }
          else {
            this.chunkCount.put(adr, Integer.valueOf(num.intValue() + 1));
          }
        }
        else {
          this.chunkCount.put(adr, Integer.valueOf(1));
        }
      }
    }
  }
}

/* Location:           D:\GitHub\MobLimiter.jar
 * Qualified Name:     MobLimiter.MyPlugin
 * JD-Core Version:    0.6.2
 */