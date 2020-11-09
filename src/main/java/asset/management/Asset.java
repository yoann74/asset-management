package asset.management;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

import java.time.Duration;

public class Asset extends AbstractBehavior<Asset.Command> {

    interface Command {
    }

    public enum CalcInterests implements Command {
        INSTANCE
    }
    public enum PrintBalance implements Command{
        INSTANCE
    }

    public static class Deposit implements Command {
        public final double amount;

        public Deposit(double amount) {
            this.amount = amount;
        }
    }

    public static class Withdraw implements Command {
        public final double amount;

        public Withdraw(double amount) {
            this.amount = amount;
        }
    }

    private static final Object TIMER_KEY = new Object();

    private Asset(ActorContext<Command> context,  double initialBalance, double interest) {
        super(context);
        this.balance = initialBalance;
        this.interest = interest;
        getContext().getLog().info("Asset {} created with an initial amount of {}", getContext().getSelf().path().name(), initialBalance);
    }

    private double balance;
    private final double interest;

    public static Behavior<Command> create(double initial, double interest) {
        return Behaviors.withTimers(
                timers -> {
                            timers.startTimerWithFixedDelay(CalcInterests.INSTANCE, Duration.ofMinutes(1));
                            return Behaviors.setup(
                                context ->
                                    new Asset(
                                    context,
                                    initial,
                                    interest));
                            }
        );
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder().onMessageEquals(PrintBalance.INSTANCE, this::onPrintBalance)
                                    .onMessageEquals(CalcInterests.INSTANCE, this::onCalcInterests)
                                    .onMessage(Deposit.class, this::onDeposit)
                                    .onMessage(Withdraw.class, this::onWithdraw)
                                    .build();
    }

    private Behavior<Command> onCalcInterests() {
        balance += balance * interest;
        getContext().getLog().info("new balance of {} is now {} (at {} interests)", getContext().getSelf().path().name(), balance, interest);
        return this;
    }

    private Behavior<Command> onPrintBalance() {
        System.out.println("current balance is " + balance);
        return this;
    }

    private Behavior<Command> onDeposit(Deposit command){
        getContext().getLog().info("Deposit of {} from {}", command.amount, getContext().getSelf().path().name());
        balance += command.amount;
        getContext().getLog().info("new balance is {}", balance);
        return this;
    }

    private Behavior<Command> onWithdraw(Withdraw command){
        getContext().getLog().info("Withdrawal of {} from {}", command.amount, getContext().getSelf().path().name());
        balance -= command.amount;
        getContext().getLog().info("new balance is {}", balance);
        return this;
    }
}
