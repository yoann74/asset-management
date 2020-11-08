package asset.management;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Asset extends AbstractBehavior<Asset.Transaction> {

    interface Transaction {
    }

    public enum PrintBalance implements Transaction{
        INSTANCE
    }

    public static class Deposit implements Transaction {
        public final double amount;

        public Deposit(double amount) {
            this.amount = amount;
        }
    }

    public static class Withdraw implements Transaction {
        public final double amount;

        public Withdraw(double amount) {
            this.amount = amount;
        }
    }

    private Asset(ActorContext<Transaction> context, double initialBalance, double interest) {
        super(context);
        this.balance = initialBalance;
        this.interest = interest;
        getContext().getLog().info("Asset {} created with an initial amount of {}", getContext().getSelf().path().name(), initialBalance);
    }

    private double balance;
    private double interest;

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
        return newReceiveBuilder().onMessageEquals(PrintBalance.INSTANCE, this::onPrintBalance)
                                    .onMessage(Deposit.class, this::onDeposit)
                                    .onMessage(Withdraw.class, this::onWithdraw)
                                    .build();
    }

    private Behavior<Transaction> onPrintBalance() {
        System.out.println("current balance is " + balance);
        return this;
    }

    private Behavior<Transaction> onDeposit(Deposit command){
        getContext().getLog().info("Deposit of {} from {}", command.amount, getContext().getSelf().path().name());
        balance += command.amount;
        getContext().getLog().info("new balance is {}", balance);
        return this;
    }

    private Behavior<Transaction> onWithdraw(Withdraw command){
        getContext().getLog().info("Withdrawal of {} from {}", command.amount, getContext().getSelf().path().name());
        balance -= command.amount;
        getContext().getLog().info("new balance is {}", balance);
        return this;
    }


}
