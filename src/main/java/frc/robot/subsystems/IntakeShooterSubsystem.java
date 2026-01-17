package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class IntakeShooterSubsystem implements Subsystem {
    TalonFX shooter = new TalonFX(Constants.IntakeShooter.shooterID);
    TalonFX intake = new TalonFX(Constants.IntakeShooter.intakeID);

    TalonFXConfiguration shooterConfig = new TalonFXConfiguration();
    TalonFXConfiguration intakeConfig = new TalonFXConfiguration();

    Slot0Configs shooterSlot0Config = shooterConfig.Slot0;
    Slot0Configs intakeSlot0Config = intakeConfig.Slot0;

    public IntakeShooterSubsystem(){
        shooterSlot0Config.kV = Constants.IntakeShooter.V;
        shooterSlot0Config.kS = Constants.IntakeShooter.S;
        shooterSlot0Config.kP = Constants.IntakeShooter.P;
        shooterSlot0Config.kI = Constants.IntakeShooter.I;
        shooterSlot0Config.kD = Constants.IntakeShooter.D;
        shooterSlot0Config.kG = Constants.IntakeShooter.G;

        shooter.getConfigurator().apply(shooterConfig);
        intake.getConfigurator().apply(intakeConfig);

    }

    public void setIntakeSpeed(double speed){
        intake.set(speed);
    }

    public void setShooterSpeed(double speed){
        shooter.set(speed);
    }

    public void stop(){
        intake.set(0);
        shooter.set(0);
    }
}
            