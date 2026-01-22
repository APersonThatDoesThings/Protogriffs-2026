package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;



@TeleOp(name = "Please use")
public class ZeMotorsNew extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {


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

            // this was a suggestion from laffiyette (weston lafollette) that we add in case
            // the robot breaks again and we're a push bot. It basically allows us to eject balls
            // from the ramp area.
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

                flywheel.setVelocity(4000/60 * 28);
                geckoWheel.setPower(1);
                intake.setPower(.75);
            }

            else if (gamepad1.right_bumper) {

                flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                flywheel.setVelocity(4000/60 * 28);
            }

            else {
                flywheel.setPower(0);
                geckoWheel.setPower(0);
                intake.setPower(0);
            }

        }

    }

}