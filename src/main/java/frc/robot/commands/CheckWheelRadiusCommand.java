package frc.robot.commands;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meter;

import java.math.RoundingMode;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveRequest.RobotCentric;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class CheckWheelRadiusCommand extends Command{
    double turnRateRad = 1;
    double robotRotations = 0;
    double wheelDistance = 0;

    double initialRotations = 0;
    double initialWheelDistance = 0;
    Angle initialWheelRotations;

    CommandSwerveDrivetrain drive;
    SwerveRequest.RobotCentric request = new SwerveRequest.RobotCentric(); 
    Supplier<Angle> position;


    double wheelRadius = 0;

    double circumference;


    public CheckWheelRadiusCommand(CommandSwerveDrivetrain drive, DoubleSupplier distanceToModule) {
        this.drive = drive;
        circumference = distanceToModule.getAsDouble()*2*Math.PI;
        position = drive.getModule(0).getDriveMotor().getRotorPosition().asSupplier();
        this.addRequirements(drive);
    }
    

    @Override
    public void initialize() {
        initialRotations = drive.getState().RawHeading.getRotations();
        initialWheelRotations = position.get();
        
    }

    @Override
    public void execute() {
        
        drive.setControl(request.withRotationalRate(turnRateRad));
        


        robotRotations = drive.getState().RawHeading.getRotations()-initialRotations;
        wheelDistance = Inches.convertFrom(drive.getState().ModulePositions[0].distanceMeters, Meter) - initialWheelDistance;
        

        double wheelRotations = position.get().minus(initialWheelRotations).magnitude()/TunerConstants.kDriveGearRatio;
        wheelDistance = robotRotations * circumference;
        wheelRadius = Math.abs((robotRotations * circumference/(2 * Math.PI * wheelRotations)));


        SmartDashboard.putNumber("Robot Rotations", robotRotations);
        SmartDashboard.putNumber("Wheel Radius", wheelRadius);
        SmartDashboard.putNumber("Wheel Distance", wheelDistance);
        SmartDashboard.putNumber("Wheel Rotations", wheelRotations);
    }

    
}
