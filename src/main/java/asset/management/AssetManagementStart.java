package asset.management;

import akka.actor.typed.ActorSystem;

import java.io.IOException;

public class AssetManagementStart {

    public static void main(String[] args) {
        //#actor-system
        final ActorSystem<AssetMain.CreateAsset> assetMain = ActorSystem.create(AssetMain.create(), "asset-management");
        //#actor-system

        //#main-send-messages
        assetMain.tell(new AssetMain.CreateAsset("Charles"));
        //#main-send-messages

        try {
            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ignored) {
        } finally {
            assetMain.terminate();
        }
    }
}
