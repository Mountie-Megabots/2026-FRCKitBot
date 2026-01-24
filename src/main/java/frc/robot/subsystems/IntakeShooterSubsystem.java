package frc.robot.subsystems;

import javax.sound.sampled.Control;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;

public class IntakeShooterSubsystem implements Subsystem {
    TalonFX hopperFeeder = new TalonFX(Constants.IntakeShooter.feederID);
    TalonFX intakeShooter = new TalonFX(Constants.IntakeShooter.intakeID);
    
    //VelocityVoltage shooterRequest = new VelocityVoltage(0);
    

    TalonFXConfiguration shooterConfig = new TalonFXConfiguration();
    TalonFXConfiguration intakeConfig = new TalonFXConfiguration();

    Slot0Configs shooterSlot0Config = shooterConfig.Slot0;
    Slot0Configs hopperFeederSlot0Config = intakeConfig.Slot0;

    public IntakeShooterSubsystem(){
        shooterSlot0Config.kV = Constants.IntakeShooter.V;
        shooterSlot0Config.kS = Constants.IntakeShooter.S;
        shooterSlot0Config.kP = Constants.IntakeShooter.P;
        shooterSlot0Config.kI = Constants.IntakeShooter.I;
        shooterSlot0Config.kD = Constants.IntakeShooter.D;
        shooterSlot0Config.kG = Constants.IntakeShooter.G;

        
        hopperFeeder.getConfigurator().apply(intakeConfig);
        intakeShooter.getConfigurator().apply(shooterConfig);
    }

    public void setIntakeSpeed(double speed){
        intakeShooter.setControl(new VelocityVoltage(speed));
    }

    public void setFeederSpeed(double speed){
        hopperFeeder.set(speed);
    }
    

    public void stop(){
        intakeShooter.set(0);
        hopperFeeder.set(0);
        
    }

    public double getShooterVelocity(){
        return intakeShooter.getVelocity().getValueAsDouble();
    }
}
            