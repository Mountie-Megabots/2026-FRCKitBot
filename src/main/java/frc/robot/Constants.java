package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class Constants {
     public static final class IntakeShooter{
        public static final int feederID = 16;
        public static final int intakeID = 15;

        public static double P = 0.25;
        public static double I = 0.1;
        public static double D = 0;
        public static double G = 0.31;
        public static double S = 0.045;
        public static double V = 0.12;
        public static double A = 0.01;
        public static double allowedClosedLoopError = 1;

        public static double lowSpeed = 20;
        public static double highSpeed = 70;
     }

      public static final class FieldConstants{
         // FIXME: Update with correct coordinates
        public static final Pose2d redHubPosition = new Pose2d(11.921, 4.02, new Rotation2d(0));
        public static final Pose2d blueHubPositon = new Pose2d(4.636, 4.02, new Rotation2d(0));
     }

      public static class DriveConstants {
        public static final double maxSpeed = 4;
        public static final double maxAngularRate = Math.PI;
    }
}
