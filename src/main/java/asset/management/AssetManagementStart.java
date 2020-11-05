package asset.management;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.Behaviors;

import java.io.IOException;

public class AssetManagementStart {

    public static void main(String[] args) {

        //#actor-system
        final ActorSystem<Asset.Transaction> assetMain = ActorSystem.create(Behaviors.empty(),"asset-management");
        //#actor-system

        final ActorRef<Asset.Transaction> saving = assetMain.systemActorOf(Asset.create(20.0,0.75), "saving", Props.empty());
        final ActorRef<Asset.Transaction> youth_saving = assetMain.systemActorOf(Asset.create(20.0, 0.5), "youth_saving", Props.empty());

        //#main-send-messages
        saving.tell(new Asset.Deposit(30.0));
        youth_saving.tell(new Asset.Withdraw(10.0));
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
