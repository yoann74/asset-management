package asset.management;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Asset extends AbstractBehavior<Asset.Transaction> {

    private Asset(ActorContext<Transaction> context, double initialBalance, double interest) {
        super(context);
        this.balance = initialBalance;
        this.interest = interest;
        getContext().getLog().info("Asset {} created with an initial amount of {}", getContext().getSelf().path().name(), initialBalance);
    }

    private double balance;
    private double interest;

    public static final class Transaction {
        public final String whom;
        public final double amount;

        public Transaction(String whom, double amount) {
            this.whom = whom;
            this.amount = amount;
        }
    }

    public static Behavior<Transaction> create(double initial, double interest) {
        return Behaviors.setup(
                context ->
                        new Asset(
                                context,
                                initial,
                                interest));
    }

    @Override
    public Receive<Transaction> createReceive() {
        return newReceiveBuilder().onMessage(Transaction.class, this::onTransaction).build();
    }

    private Behavior<Transaction> onTransaction(Transaction command){
        getContext().getLog().info("Transaction of {} from {} on {}", command.amount, command.whom, getContext().getSelf().path().name());
        balance += command.amount;
        getContext().getLog().info("new balance is {}", balance);
        return this;
    }
}
