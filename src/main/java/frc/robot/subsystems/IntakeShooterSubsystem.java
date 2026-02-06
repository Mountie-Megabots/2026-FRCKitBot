package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import javax.sound.sampled.Control;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class IntakeShooterSubsystem implements Subsystem {
    TalonFX hopperFeeder = new TalonFX(Constants.IntakeShooter.feederID);
    TalonFX intakeShooter = new TalonFX(Constants.IntakeShooter.intakeID);

    // VelocityVoltage shooterRequest = new VelocityVoltage(0);

    TalonFXConfiguration shooterConfig = new TalonFXConfiguration();
    TalonFXConfiguration intakeConfig = new TalonFXConfiguration();

    Slot0Configs shooterSlot0Config = shooterConfig.Slot0;
    Slot0Configs hopperFeederSlot0Config = intakeConfig.Slot0;

    private enum intakeState {
        neutral, intake, shoot
    };

    private intakeState currentState = intakeState.neutral;

    private double feedSpeed = 0;

    public IntakeShooterSubsystem() {
        shooterSlot0Config.kV = Constants.IntakeShooter.V;
        shooterSlot0Config.kS = Constants.IntakeShooter.S;
        shooterSlot0Config.kP = Constants.IntakeShooter.P;
        shooterSlot0Config.kI = Constants.IntakeShooter.I;
        shooterSlot0Config.kD = Constants.IntakeShooter.D;
        shooterSlot0Config.kG = Constants.IntakeShooter.G;

        hopperFeeder.getConfigurator().apply(intakeConfig);
        intakeShooter.getConfigurator().apply(shooterConfig);

        
    }

    

    public void runStateMachine() {
        SmartDashboard.putNumber("Shooter Speed", Math.abs(getShooterVelocity()));
        switch (currentState) {
            case neutral:
                setIntakeSpeed(0);
                setFeederSpeed(0);
                break;

            case intake:
                setIntakeSpeed(0.5);
                setFeederSpeed(-0.3);
                break;

            case shoot:
                setIntakeVelocity(Constants.IntakeShooter.highSpeed);
                if (Math.abs(getShooterVelocity()) > Math.abs((Constants.IntakeShooter.highSpeed * .8))) {
                    setFeederSpeed(feedSpeed);
                } else {
                    setFeederSpeed(0);
                }
                break;
        }
    }

    public void setIntakeState() {
        if (currentState == intakeState.neutral) {
            currentState = intakeState.intake;
        } else if (currentState == intakeState.intake) {
            currentState = intakeState.neutral;
        } else if (currentState == intakeState.shoot) {
            currentState = intakeState.intake;
        }
    }

    public void setShootState() {
        if (currentState == intakeState.neutral || currentState == intakeState.intake) {
            currentState = intakeState.shoot;
        } else if (currentState == intakeState.shoot) {
            currentState = intakeState.neutral;
        }
    }

    public void setNeutral(){
        currentState = intakeState.neutral;
    }

    public Command setNeutralCommand() {
        return new InstantCommand(this::setNeutral);
    }

    public Command setIntakeStateCommand() {
        return new InstantCommand(() -> setIntakeState());
    }

    public Command setShootStateCommand() {
        return new InstantCommand(() -> setShootState());
    }

    public RunCommand setFeedSpeed(DoubleSupplier speed) {
        return new RunCommand(() -> feedSpeed = speed.getAsDouble());
    }


    public void setIntakeVelocity(double velocity) {
        intakeShooter.setControl(new VelocityVoltage(velocity));
    }

    public void setIntakeSpeed(double speed) {
        intakeShooter.set(speed);
    }

    public void setFeederSpeed(double speed) {
        hopperFeeder.set(speed);
    }

    public void stop() {
        intakeShooter.set(0);
        hopperFeeder.set(0);

    }

    public double getShooterVelocity() {
        return intakeShooter.getVelocity().getValueAsDouble();
    }
}
