package frc.robot.commands;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meter;

import java.math.RoundingMode;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveRequest.RobotCentric;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class CheckWheelRadiusCommand extends Command{
    double turnRateRad = 1;
    double circumference;

    boolean radiusCalcInit = false;

    CommandSwerveDrivetrain drive;
    SwerveRequest.RobotCentric request = new SwerveRequest.RobotCentric(); 
    Supplier<Angle> position;


    


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
        

        double averageRadius = (calculateWheelRadius(0) + calculateWheelRadius(1) + calculateWheelRadius(2) + calculateWheelRadius(3))/4;

        SmartDashboard.putNumber("Average Wheel Radius", averageRadius);

        radiusCalcInit = true;
        
    }

    public double calculateWheelRadius(int wheelIndex) {
        Angle initialWheelRotations = null;
        double initialRotations = 0;
        double initialWheelDistance = 0;

        if (!radiusCalcInit) {
             initialRotations = drive.getState().RawHeading.getRotations();
             initialWheelRotations = position.get();
             initialWheelDistance = Inches.convertFrom(drive.getState().ModulePositions[wheelIndex].distanceMeters, Meter);
        }
        
        double wheelRadius = 0;
        double robotRotations = 0;
        double wheelDistance = 0;
        double circumference = 0;
        
        position = drive.getModule(wheelIndex).getDriveMotor().getRotorPosition().asSupplier();

        robotRotations = drive.getState().RawHeading.getRotations()-initialRotations;
        wheelDistance = Inches.convertFrom(drive.getState().ModulePositions[wheelIndex].distanceMeters, Meter) - initialWheelDistance;
        

        double wheelRotations = position.get().minus(initialWheelRotations).magnitude()/TunerConstants.kDriveGearRatio;
        wheelDistance = robotRotations * circumference;
        wheelRadius = Math.abs((robotRotations * circumference/(2 * Math.PI * wheelRotations)));

        SmartDashboard.putNumber("Radius of " + wheelIndex, wheelRadius);

        return wheelRadius;

        // SmartDashboard.putNumber("Robot Rotations", robotRotations);
        
        // SmartDashboard.putNumber("Wheel Distance", wheelDistance);
        // SmartDashboard.putNumber("Wheel Rotations", wheelRotations);
    }
    
}
