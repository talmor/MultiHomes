/*
 * MultiHomes Plugin (c) 2011 Talmor mc_plugins@obscurafx.com
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class MultiHomes extends Plugin {
    static final MHcmdListener      cmdListener       = new MHcmdListener();
    static PluginRegisteredListener regCmdListener    = null;
    static PluginRegisteredListener regPlayerListener = null;
    public static ArrayList<String> mutedplayers      = new ArrayList<String>();
    private static Logger           log               = Logger.getLogger("Minecraft");
    private String                  name              = "MultiHomes";
    private String                  version           = "0.1";

    @Override
    public void disable() {
        if (MultiHomes.regCmdListener != null) {
            etc.getLoader().removeListener(MultiHomes.regCmdListener);
            MultiHomes.regCmdListener = null;
        }
    }

    @Override
    public void enable() {
        if (MultiHomes.regCmdListener == null) {
            MultiHomes.regCmdListener = etc.getLoader().addListener(PluginLoader.Hook.COMMAND, cmdListener, this, PluginListener.Priority.MEDIUM);
        }
    }

    public void initialize() {
        log.info(name + " " + version + " initialized");
        MHPreferences.initialize();
        LinkedHashMap<String, String> commands = etc.getInstance().getCommands();
        for (String[] command : MHcmdListener.commands) {
            commands.put("/" + command[0].toLowerCase(), command[1]);
        }

    }

}
