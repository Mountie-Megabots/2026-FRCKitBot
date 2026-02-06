package frc.robot.commands;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveRequest.RobotCentric;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class OsscilateCommand extends Command{
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);
    private RobotCentric osscilateRequest = new SwerveRequest.RobotCentric();
    private Timer timer = new Timer();
    private CommandSwerveDrivetrain drive;

    private double magnitude = 0.3;
    private double osscilationsPerSecond = 20;

    public OsscilateCommand(CommandSwerveDrivetrain driveConstruct) {
        drive = driveConstruct;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        //drive.applyRequest(() -> osscilateRequest.withRotationalRate(magnitude*(Math.sin(Math.PI*osscilationsPerSecond*timer.get()))*MaxAngularRate));
        drive.setControl(osscilateRequest.withRotationalRate(magnitude*(Math.sin(Math.PI*osscilationsPerSecond*timer.get()))*MaxAngularRate));
        
    }


}
