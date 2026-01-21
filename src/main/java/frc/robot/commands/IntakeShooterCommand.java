package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeShooterSubsystem;

public class IntakeShooterCommand extends Command {
    IntakeShooterSubsystem intake;
    DoubleSupplier intakeSpeed;
    DoubleSupplier shootSpeed;
    

    public IntakeShooterCommand(IntakeShooterSubsystem intake, DoubleSupplier intakeSpeed, DoubleSupplier shootSpeed) {
        this.intakeSpeed = intakeSpeed;
        this.shootSpeed = shootSpeed;
        this.intake = intake;
        
        this.addRequirements(intake);
    }

    public void execute() {
        if (intakeSpeed.getAsDouble() > shootSpeed.getAsDouble()) {
            intake.setIntakeSpeed(Constants.IntakeShooter.lowSpeed);
            intake.setFeederSpeed(-intakeSpeed.getAsDouble());
        } else if(intakeSpeed.getAsDouble() < shootSpeed.getAsDouble()){
            intake.setIntakeSpeed(Constants.IntakeShooter.highSpeed);
            intake.setFeederSpeed(shootSpeed.getAsDouble());
        } else {
            intake.setFeederSpeed(0);
            intake.setIntakeSpeed(0);
        }

    }

    public void end() {
        intake.stop();
    }
}
