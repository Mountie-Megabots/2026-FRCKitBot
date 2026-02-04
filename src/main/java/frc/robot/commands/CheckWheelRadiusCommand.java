package frc.robot.commands;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Meter;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveRequest.RobotCentric;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.ImmutableAngle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class CheckWheelRadiusCommand extends Command {
    double turnRateRad = 1;
    double circumference;

    boolean radiusCalcInit = false;

    CommandSwerveDrivetrain drive;
    SwerveRequest.RobotCentric request = new SwerveRequest.RobotCentric();

    double initialHeading = 0;
    Angle[] initialEncoder = new Angle[4];
    double[] wheelRadii = {0,0,0,0};

    List<Supplier<Angle>> encoders = new ArrayList<>(4);

    public CheckWheelRadiusCommand(CommandSwerveDrivetrain drive, DoubleSupplier distanceToModule) {
        this.drive = drive;
        circumference = distanceToModule.getAsDouble() * 2 * Math.PI;

        this.addRequirements(drive);
    }

    @Override
    public void initialize() {
        initialHeading = drive.getState().RawHeading.getRotations();

        for(int i =0; i < 4; i++){
            encoders.add(drive.getModule(i).getDriveMotor().getRotorPosition().asSupplier());
            initialEncoder[i] = encoders.get(i).get();
        }
    }

    @Override
    public void execute() {

        drive.setControl(request.withRotationalRate(turnRateRad));

        // double averageRadius = (calculateWheelRadius(0) + calculateWheelRadius(1) + calculateWheelRadius(2)
        //         + calculateWheelRadius(3)) / 4;
        double gyro = drive.getState().RawHeading.getRotations();

        double botRotations = gyro - initialHeading;

        for(int i = 0; i < 4; i++){
            double wheelRotations = encoders.get(i).get().minus((initialEncoder[i])).magnitude()/TunerConstants.kDriveGearRatio;
            wheelRadii[i] = Math.abs((botRotations * circumference / (2 * Math.PI * wheelRotations)));
            SmartDashboard.putNumber("Wheel Radius of " + i, wheelRadii[i]);
            // Write to network Tables
        }

        SmartDashboard.putNumber("Average Wheel Radius", (wheelRadii[0] + wheelRadii[1] + wheelRadii[2] + wheelRadii[3])/4);

        radiusCalcInit = true;

    }
}
