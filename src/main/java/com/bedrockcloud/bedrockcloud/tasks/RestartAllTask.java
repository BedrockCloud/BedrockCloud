package com.bedrockcloud.bedrockcloud.tasks;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerTextPacket;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class RestartAllTask extends TimerTask
{

    protected static boolean send1;
    protected static boolean send2;
    protected static boolean send3;

    public RestartAllTask(){
        send1 = false;
        send2 = false;
        send3 = false;
    }

    @Override
    public void run() {

        if (!BedrockCloud.isRunning()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date();

        if (sdf.format(timestamp).equals("01:55:00")){
            if (!send1) {
                send1 = true;
                PlayerTextPacket pk = new PlayerTextPacket();
                pk.playerName = "all.players";
                pk.type = pk.TYPE_MESSAGE;
                pk.value = BedrockCloud.prefix + "§cThe server is restarting in §45 minutes§8.";
                for (CloudPlayer player : BedrockCloud.getCloudPlayerProvider().getCloudPlayerMap().values()) {
                    player.getProxy().pushPacket(pk);
                }
            }
        } else if (sdf.format(timestamp).equals("01:59:00")){
            if (!send2) {
                send2 = true;
                PlayerTextPacket pk = new PlayerTextPacket();
                pk.playerName = "all.players";
                pk.type = pk.TYPE_MESSAGE;
                pk.value = BedrockCloud.prefix + "§cThe server is restarting in §41 minute§8.";
                for (CloudPlayer player : BedrockCloud.getCloudPlayerProvider().getCloudPlayerMap().values()) {
                    player.getProxy().pushPacket(pk);
                }
            }
        } else if (sdf.format(timestamp).equals("02:00:00") || sdf.format(timestamp).equals("02:00:01") || sdf.format(timestamp).equals("02:00:02")) {
            if (!send3) {
                send3 = true;
                PlayerTextPacket pk = new PlayerTextPacket();
                pk.playerName = "all.players";
                pk.type = pk.TYPE_MESSAGE;
                pk.value = BedrockCloud.prefix + "§cThe server is restarting §4now§8.";
                for (CloudPlayer player : BedrockCloud.getCloudPlayerProvider().getCloudPlayerMap().values()) {
                    player.getProxy().pushPacket(pk);
                }

                BedrockCloud.getLogger().warning("§cRestarting all gameserver.");
                BedrockCloud.setRunning(false);
                System.exit(0);
            }
        }
    }
}
