package asset.management;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class AssetMain extends AbstractBehavior<AssetMain.CreateAsset> {


    public static class CreateAsset {
        public final String owner;

        public CreateAsset(String owner) {
            this.owner = owner;
        }
    }

    private final ActorRef<Asset.Transaction> saving;
    private final ActorRef<Asset.Transaction> youth_saving;

    public static Behavior<CreateAsset> create() {
        return Behaviors.setup(AssetMain::new);
    }

    private AssetMain(ActorContext<CreateAsset> context) {
        super(context);
        //#create-actors
        saving = context.spawn(Asset.create(20.0, 0.75), "saving");
        youth_saving = context.spawn(Asset.create(20.0, 1), "youth_saving");
        //#create-actors
    }

    @Override
    public Receive<CreateAsset> createReceive() {
        return newReceiveBuilder().onMessage(CreateAsset.class, this::onCreateAsset).build();
    }

    private Behavior<CreateAsset> onCreateAsset(CreateAsset command) {
        //#create-actors
        saving.tell(new Asset.Transaction("Sylvain", 30));
        youth_saving.tell(new Asset.Transaction("Arthur", 50));
        //#create-actors
        return this;
    }
}
