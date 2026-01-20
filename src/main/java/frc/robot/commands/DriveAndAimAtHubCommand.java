package frc.robot.commands;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class DriveAndAimAtHubCommand extends Command {

    private final CommandSwerveDrivetrain drivetrain;
    private final DoubleSupplier xSpeed;
    private final DoubleSupplier ySpeed;

    private DriverStation.Alliance alliance = DriverStation.Alliance.Blue;
    private Pose2d hubPosition;

    private double allianceOffsetDeg;
    private double allianceInversionFactor;

    private final SwerveRequest.FieldCentricFacingAngle drive =
            new SwerveRequest.FieldCentricFacingAngle()
                    .withDeadband(Constants.DriveConstants.maxSpeed * 0.1)
                    .withRotationalDeadband(Constants.DriveConstants.maxAngularRate * 0.1)
                    .withDriveRequestType(DriveRequestType.OpenLoopVoltage)
                    .withSteerRequestType(SteerRequestType.MotionMagicExpo);

    public DriveAndAimAtHubCommand(
            CommandSwerveDrivetrain drivetrain,
            DoubleSupplier xSpeed,
            DoubleSupplier ySpeed
    ) {
        this.drivetrain = drivetrain;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;

        drive.HeadingController.setP(10.0);
        drive.HeadingController.enableContinuousInput(-Math.PI, Math.PI);

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        alliance = determineAlliance();

        if (alliance == DriverStation.Alliance.Blue) {
            hubPosition = Constants.FieldConstants.blueHubPositon;
            allianceOffsetDeg = 0.0;
            allianceInversionFactor = -1.0;
        } else {
            hubPosition = Constants.FieldConstants.redHubPosition;
            allianceOffsetDeg = 180.0;
            allianceInversionFactor = 1.0;
        }

        SmartDashboard.putString("AAH - Alliance", alliance.name());
    }

    @Override
    public void execute() {
        Pose2d robotPose = drivetrain.getState().Pose;

        // Vector from robot to hub
        double dx = hubPosition.getX() - robotPose.getX();
        double dy = hubPosition.getY() - robotPose.getY();
        double angleToHubRad = Math.atan2(dy, dx);

        // Driver translation input
        Translation2d translation =
                new Translation2d(
                        xSpeed.getAsDouble(),
                        ySpeed.getAsDouble())
                        .times(Constants.DriveConstants.maxSpeed * allianceInversionFactor)
                        .rotateBy(robotPose.getRotation());

        // Heading error (for logging only)
        Rotation2d headingError =
                Rotation2d.fromRadians(angleToHubRad).minus(robotPose.getRotation());

        SmartDashboard.putNumber("AAH - error (deg)", headingError.getDegrees());
        SmartDashboard.putNumber("AAH - angleToHub (rad)", angleToHubRad);

        drivetrain.setControl(
                drive.withVelocityX(translation.getX())
                     .withVelocityY(translation.getY())
                     .withTargetDirection(
                             Rotation2d.fromRadians(angleToHubRad)
                                       .plus(Rotation2d.fromDegrees(allianceOffsetDeg)))
        );
    }

    /**
     * Determines alliance color.
     * In simulation, DriverStation always reports Red, so use FMSInfo.
     */
    private DriverStation.Alliance determineAlliance() {
        if (Utils.isSimulation()) {
            NetworkTableEntry isRed =
                    NetworkTableInstance.getDefault()
                            .getEntry("/FMSInfo/IsRedAlliance");
            return isRed.getBoolean(false)
                    ? DriverStation.Alliance.Red
                    : DriverStation.Alliance.Blue;
        }

        return DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue);
    }
}

