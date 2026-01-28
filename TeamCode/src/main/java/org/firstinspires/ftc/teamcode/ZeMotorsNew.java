package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.robotcore.external.Telemetry;


@TeleOp(name = "Newest TeleOp")
public class ZeMotorsNew extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        //this is a test

        //This is a test:

        // Intake (obviously)
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");

        // Gecko Wheel setup
        DcMotor geckoWheel = hardwareMap.get(DcMotor.class, "geckoWheel");

        // Rhino wheel setup
        DcMotorEx flywheel = (DcMotorEx) hardwareMap.get(DcMotor.class, "flywheel");

        //Drivetrain
        Drivetrain drivetrain = new Drivetrain(hardwareMap);
        drivetrain.initOpMode();

        boolean isLeftTriggerDown = false;

        waitForStart();

        while (this.opModeIsActive()) {

            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            drivetrain.drive(drive, strafe, turn);

            // store velocity
            double currentFlywheelVelo = flywheel.getVelocity();

            // declare and initialize default for velocity (0)
            double oldFlywheelVelo = 0;

            // check if current velocity is different from our old velocity
            // if so, set the old velocity to current and tell DS to tell drivers
            // what the current velocity is
            // Also checks if velo is 4k and notifies that it is ready to fire

            // WARNING: I HAVE NO CLUE HOW THE TICKS THINGS WORK SO IMA HAVE TO PLAY WITH IT
            // so rn basically this is an indev feature and will eventually work right
            // cheese incorporated (c) 2026
            if (currentFlywheelVelo != oldFlywheelVelo) {
                oldFlywheelVelo = currentFlywheelVelo;
                telemetry.addData("Current Flywheel Velocity", currentFlywheelVelo);
                if ((int) currentFlywheelVelo == 4000) {
                    telemetry.addLine("Fire when ready!");
                }
            }

            if (gamepad1.left_trigger >= 0.5) {
                isLeftTriggerDown = true;
            }

            else { isLeftTriggerDown = false; }

            // Turn on intake
            if (gamepad1.right_trigger >= 0.5) {

                intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                intake.setPower(.75);
            }
            else {
                intake.setPower(0);
            }

            // If statement for launcher activation
            if (gamepad1.left_trigger >= 0.5) {

                intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                geckoWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                flywheel.setVelocity(4000/60 * 28);
                geckoWheel.setPower(1);
                intake.setPower(1);

            }

            else if(!gamepad1.right_bumper) {
                flywheel.setPower(0);
                geckoWheel.setPower(0);
                intake.setPower(0);
            }

            // Cycle artifacts through the "system"
            // forget if we need to change the speed or not
            if (gamepad1.y) {
                geckoWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                intake.setPower(0.4);
                geckoWheel.setPower(0.4);
            }
            else {
                intake.setPower(0);
                geckoWheel.setPower(0);
            }

            // artifact removal code
            if (gamepad1.b) {
                intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                geckoWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                intake.setPower(-0.5);
                geckoWheel.setPower(-0.5);
            }

            else {
                intake.setPower(0);
                geckoWheel.setPower(0);
            }

            if (gamepad1.right_bumper && isLeftTriggerDown) {
                intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                geckoWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                flywheel.setVelocity(4000 / 60 * 28);
                geckoWheel.setPower(1);
                intake.setPower(.75);
            }

            else if (gamepad1.right_bumper) {

                flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                flywheel.setVelocity(4000 / 60 * 28);
            }

            else {
                flywheel.setPower(0);
                geckoWheel.setPower(0);
                intake.setPower(0);
            }
            telemetry.update();
        }
    }
}