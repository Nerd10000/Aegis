package dragon.me.Aegis.checks;

import dragon.me.Aegis.Aegis;
import org.bukkit.configuration.file.FileConfiguration;

public class Check {


    private  String checkName;
    public Check(String checkName){

            this.checkName = checkName;
    }



    public String getCheckName() {
        return checkName;
    }

    public FileConfiguration getConfig(){
        return Aegis.getPlugin().getConfig();
    }
}
