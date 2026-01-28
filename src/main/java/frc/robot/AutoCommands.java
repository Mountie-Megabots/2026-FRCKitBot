package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.IntakeShooterCommand;
import frc.robot.subsystems.IntakeShooterSubsystem;

public class AutoCommands {
    private SendableChooser<Command> autoChooser;
    private IntakeShooterSubsystem intake;

    public AutoCommands(IntakeShooterSubsystem constructIntake) {
        intake = constructIntake;
        // Build an auto chooser. This will use Commands.none() as the default option.
        
        NamedCommands.registerCommand("ShootPrep", intake.setShootStateCommand());
        NamedCommands.registerCommand("Shoot", intake.setFeedSpeed(() -> 1.0));
        autoChooser = AutoBuilder.buildAutoChooser();
        // Another option that allows you to specify the default auto by its name
        // autoChooser = AutoBuilder.buildAutoChooser("My Default Auto");

        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
