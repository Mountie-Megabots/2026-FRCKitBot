package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeShooterSubsystem;

public class IntakeShooterCommand extends Command {
    IntakeShooterSubsystem intake;
    DoubleSupplier intakeSpeed;
    DoubleSupplier shootSpeed;
    double setFeedSpeed = 0;
    

    public IntakeShooterCommand(IntakeShooterSubsystem intake) {
        this.intakeSpeed = intakeSpeed;
        this.shootSpeed = shootSpeed;
        this.intake = intake;
        
        this.addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.setNeutral();
    }

    @Override
    public void execute() {
        intake.runStateMachine();
    }

    public void end() {
        intake.stop();
    }
}
