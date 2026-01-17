package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeShooterSubsystem;

public class IntakeShooterCommand extends Command{
    IntakeShooterSubsystem intake;
    DoubleSupplier intakeSpeed;
    DoubleSupplier shootSpeed;

    public IntakeShooterCommand(IntakeShooterSubsystem intake, DoubleSupplier intakeSpeed, DoubleSupplier shootSpeed){
        this.intakeSpeed = intakeSpeed;
        this.shootSpeed = shootSpeed;
        this.intake = intake;

        this.addRequirements(intake);
    }

    public void execute(){
        intake.setIntakeSpeed(intakeSpeed.getAsDouble());
        intake.setShooterSpeed(shootSpeed.getAsDouble());
    }

    public void end(){
        intake.stop();
    }
}
