package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.robotcore.external.Telemetry;


@TeleOp(name = "Newest TeleOp")
public class ZeMotorsNew extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // IMPORTANT: link for documentation if you want to learn stuff yourself
        // https://javadoc.io/doc/org.firstinspires.ftc
        // or just google a specific class if you cant find it
        // ex. for motors just google "ftc motor class" and it should show
        // docs for dcmotor (or just google what you need to do and add ftc and it should
        // work fine

        // Intake (obviously)
        DcMotorEx intake = (DcMotorEx) hardwareMap.get(DcMotor.class, "intake");
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);


        // Gecko Wheel setup
        DcMotor geckoWheel = hardwareMap.get(DcMotor.class, "geckoWheel");
        geckoWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Rhino wheel setup
        DcMotorEx flywheel = (DcMotorEx) hardwareMap.get(DcMotor.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Drivetrain
        Drivetrain drivetrain = new Drivetrain(hardwareMap);
        drivetrain.initOpMode();

        double maxFlywheelSpeed = 2000 * 28 / 60.0;


        waitForStart();
        intake.setPower(1);

        while (this.opModeIsActive()) {

            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            drivetrain.drive(drive, strafe, turn);

            // check if current velocity is different from our old velocity
            // if so, set the old velocity to current and tell DS to tell drivers
            // what the current velocity is
            // Also checks if velo is 4k and notifies that it is ready to fire

            // TODO: check flywheel rpm and change this over to angle units (rotations / s
            telemetry.addData("Current Flywheel Velocity", -flywheel.getVelocity() * (60 / 28.0));
            if (-(flywheel.getVelocity() * (60 / 28.0)) >= 2000) {
                telemetry.addLine("Fire when ready!");
            }

            // Turn on intake
            if (gamepad1.right_trigger >= 0.5) {
                intake.setPower(1);
            }
            else {
                intake.setVelocity(0);
            }

            // If statement for launcher activation
            if (gamepad1.left_trigger >= 0.5 && (-flywheel.getVelocity() * (60 / 28.0) <= 2200)) {
                flywheel.setVelocity(maxFlywheelSpeed);
                geckoWheel.setPower(1);
                intake.setPower(1);
                intake.setVelocity(1000);

            }
            else if (gamepad1.right_bumper && (-flywheel.getVelocity() * (60 / 28.0) <= 2200)) {
                flywheel.setVelocity(maxFlywheelSpeed);
            }

            else {
                flywheel.setPower(0);
                geckoWheel.setPower(0);
                intake.setVelocity(0);
            }

            // Cycle artifacts through the "system"
            // forget if we need to change the speed or not
            if (gamepad1.y) {
                intake.setVelocity(1000);
                geckoWheel.setPower(1);
            }
            else {
                intake.setVelocity(0);
                geckoWheel.setPower(0);
            }

            // artifact removal code
            if (gamepad1.b) {
                intake.setVelocity(-1000);
                geckoWheel.setPower(-0.5);
            }

            else {
                intake.setPower(0);
                geckoWheel.setPower(0);
            }

            telemetry.update();
        }
    }
}