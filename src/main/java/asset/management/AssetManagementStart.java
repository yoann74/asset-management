package asset.management;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.Behaviors;

import java.io.IOException;

public class AssetManagementStart {

    public static void main(String[] args) {

        //#actor-system
        final ActorSystem<Asset.Command> assetMain = ActorSystem.create(Behaviors.empty(),"asset-management");
        //#actor-system

        final ActorRef<Asset.Command> saving = assetMain.systemActorOf(Asset.create(20.0,0.075), "saving", Props.empty());
        final ActorRef<Asset.Command> youth_saving = assetMain.systemActorOf(Asset.create(20.0, 0.05), "youth_saving", Props.empty());

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
