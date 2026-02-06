package frc.robot;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.CheckWheelRadiusCommand;
import frc.robot.commands.IntakeShooterCommand;
import frc.robot.commands.OsscilateCommand;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeShooterSubsystem;

public class AutoCommands {
    private SendableChooser<Command> autoChooser;
    private IntakeShooterSubsystem intake;
    private CommandSwerveDrivetrain drive;

    

    public AutoCommands(IntakeShooterSubsystem constructIntake, CommandSwerveDrivetrain constructDrive) {
        intake = constructIntake;
        drive = constructDrive;
        // Build an auto chooser. This will use Commands.none() as the default option.
        
        NamedCommands.registerCommand("ShootPrep", intake.setShootStateCommand());
        NamedCommands.registerCommand("Shoot", intake.setFeedSpeed(() -> 1.0));
        NamedCommands.registerCommand("Intake", intake.setIntakeStateCommand());
        NamedCommands.registerCommand("Osscilate", new OsscilateCommand(drive));
        autoChooser = AutoBuilder.buildAutoChooser();
        //Frame size: Inches
        autoChooser.addOption("CheckWheelRadius", new CheckWheelRadiusCommand(drive, () -> 13.25));
        // Another option that allows you to specify the default auto by its name
        // autoChooser = AutoBuilder.buildAutoChooser("My Default Auto");

        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

    
}
