package frc.robot.commands;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meter;

import java.math.RoundingMode;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveRequest.RobotCentric;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class CheckWheelRadiusCommand extends Command{
    double turnRateRad = 1;
    double robotRotations = 0;
    double wheelDistance = 0;

    CommandSwerveDrivetrain drive;
    SwerveRequest.RobotCentric request = new SwerveRequest.RobotCentric(); 

    double wheelRadius = 0;

    double circumference;

    public CheckWheelRadiusCommand(CommandSwerveDrivetrain drive, DoubleSupplier distanceToModule) {
        this.drive = drive;
        circumference = distanceToModule.getAsDouble()*2*Math.PI;
        this.addRequirements(drive);
    }
    

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        
        drive.setControl(request.withRotationalRate(turnRateRad));
        
        SmartDashboard.putNumber("Robot Rotations", robotRotations);
        SmartDashboard.putNumber("Wheel Radius", wheelRadius);
        SmartDashboard.putNumber("Wheel Distance", wheelDistance);

        robotRotations = drive.getState().RawHeading.getRotations();

        wheelDistance = Inches.convertFrom(drive.getState().ModulePositions[0].distanceMeters, Meter); 

        wheelRadius = (robotRotations * circumference/(2 * Math.PI * wheelDistance));
        //wheelRadius = ((robotRotations*circumference)/wheelDistance)/(2*Math.PI);
    }

    
}
