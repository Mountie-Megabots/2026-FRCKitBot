package frc.robot.commands;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

/**
 * This command lets the driver move the robot while
 * the robot automatically turns to face the hub.
 *
 * STUDENT TASK:
 * You will fill in the missing pieces so the robot:
 *  - Figures out which alliance it is on
 *  - Finds the hub on the field
 *  - Calculates the angle to the hub
 *  - Drives while facing the hub
 */
public class DriveAndAimAtHubCommand extends Command {

    // The drivetrain subsystem
    private final CommandSwerveDrivetrain drivetrain;

    // Joystick inputs (forward/back and left/right)
    private final DoubleSupplier xSpeed;
    private final DoubleSupplier ySpeed;

    // Which alliance are we on? (Red or Blue)
    private DriverStation.Alliance alliance;

    // The position of the hub on the field
    private Pose2d hubPosition;

    // These values change depending on alliance
    private double allianceOffsetDeg;
    private double allianceInversionFactor;

    // This is the CTRE swerve request we send every loop
    private final SwerveRequest.FieldCentricFacingAngle drive =
            new SwerveRequest.FieldCentricFacingAngle()
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

        // Tell WPILib that this command uses the drivetrain
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {

        /*
         * TODO 1: Figure out which alliance we are on
         *
         * Hint:
         *  - Use DriverStation.getAlliance()
         *  - If it does not exist, default to Blue
         */

        alliance = DriverStation.getAlliance().get();

        /*
         * TODO 2: Based on the alliance:
         *  - Set hubPosition
         *  - Set allianceOffsetDeg
         *  - Set allianceInversionFactor
         *
         * Hints:
         *  - Blue and Red hubs are in Constants.FieldConstants
         *  - Red alliance usually needs a 180 degree offset
         *  - One alliance needs its joystick directions flipped
         */
        if (alliance == Alliance.Blue) {
            hubPosition = Constants.FieldConstants.blueHubPositon;
            allianceInversionFactor = 1;
            allianceOffsetDeg = 0;
        } else {
            hubPosition = Constants.FieldConstants.redHubPosition;
            allianceInversionFactor = -1;
            allianceOffsetDeg = 180;
        }
    }

    @Override
    public void execute() {

        /*
         * TODO 3: Get the robot's current position on the field
         *
         * Hint:
         *  - drivetrain.getState().Pose
         */
        Pose2d robotPose = drivetrain.getState().Pose;

        /*
         * TODO 4: Find the X and Y distance from the robot to the hub
         *
         * Hint:
         *  - dx = hubX - robotX
         *  - dy = hubY - robotY
         */
        double dx = hubPosition.getX() - robotPose.getX();
        double dy = hubPosition.getY() - robotPose.getY();

        /*
         * TODO 5: Use atan2 to calculate the angle to the hub (in radians)
         *
         * Hint:
         *  - Math.atan2(y, x)
         */
        double angleToHubRad = Math.atan2(dy, dx);

        /*
         * TODO 6: Create a Translation2d from joystick inputs
         *
         * Steps:
         *  - Read xSpeed and ySpeed
         *  - Multiply by max speed
         *  - Apply alliance inversion
         *  - Rotate by the robot's current rotation
         */
        Translation2d translation = new Translation2d(xSpeed.getAsDouble()*Constants.DriveConstants.maxSpeed*allianceInversionFactor, 
            ySpeed.getAsDouble()*Constants.DriveConstants.maxSpeed*allianceInversionFactor)
            .rotateBy(Rotation2d.fromDegrees(angleToHubRad));

        /*
         * TODO 7: Send the command to the drivetrain
         *
         * You must:
         *  - Set velocity X
         *  - Set velocity Y
         *  - Set the target direction (angle to hub + alliance offset)
         */
        drivetrain.setControl(
                drive
                    .withVelocityX(translation.getX())
                    .withVelocityY(translation.getY())
                    .withTargetDirection(translation.getAngle())
        );
    }
}
