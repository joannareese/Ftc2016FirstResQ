package ftc3543;

import ftclib.FtcDcMotor;
import ftclib.FtcServo;
import ftclib.FtcTouch;
import trclib.TrcDigitalTrigger;
import trclib.TrcMotorController;
import trclib.TrcEvent;
import trclib.TrcMotorLimitSwitches;
import trclib.TrcPidController;
import trclib.TrcPidMotor;

public class Elevator implements TrcPidController.PidInput,
                                 TrcMotorLimitSwitches,
                                 TrcDigitalTrigger.TriggerHandler
{
    //
    // This component consists of an elevator motor, a lower limit switch,
    // an upper limit switch, an encoder to keep track of the position of
    // the elevator and a servo to engage/disengage the chain lock.
    //
    private FtcDcMotor motor;
    private TrcPidController pidController;
    private TrcPidMotor pidMotor;
    private FtcTouch lowerLimitSwitch;
    private FtcTouch upperLimitSwitch;
    private TrcDigitalTrigger lowerLimitTrigger;
    private FtcServo chainLock;

    public Elevator()
    {
        motor = new FtcDcMotor("elevator", this);
        pidController = new TrcPidController(
                "elevator",
                RobotInfo.ELEVATOR_KP, RobotInfo.ELEVATOR_KI,
                RobotInfo.ELEVATOR_KD, RobotInfo.ELEVATOR_KF,
                RobotInfo.ELEVATOR_TOLERANCE,RobotInfo.ELEVATOR_SETTLING,
                this);
        pidController.setAbsoluteSetPoint(true);
        pidMotor = new TrcPidMotor("elevator", motor, pidController);
        pidMotor.setPositionScale(RobotInfo.ELEVATOR_INCHES_PER_CLICK);
        lowerLimitSwitch = new FtcTouch("lowerLimitSwitch");
        upperLimitSwitch = new FtcTouch("upperLimitSwitch");
        lowerLimitTrigger = new TrcDigitalTrigger("elevatorLowerLimit", lowerLimitSwitch, this);
        lowerLimitTrigger.setEnabled(true);
        chainLock = new FtcServo("chainLock");
        setChainLock(false);
    }

    public void zeroCalibrate(double calPower)
    {
        pidMotor.zeroCalibrate(calPower);
    }

    public void setChainLock(boolean locked)
    {
        chainLock.setPosition(
                locked? RobotInfo.CHAINLOCK_LOCK_POSITION: RobotInfo.CHAINLOCK_UNLOCK_POSITION);
    }

    public void setPower(double power)
    {
        pidMotor.setPower(power);
    }

    public void setHeight(double height)
    {
        pidMotor.setTarget(height, true);
    }

    public void setHeight(double height, TrcEvent event, double timeout)
    {
        pidMotor.setTarget(height, event, timeout);
    }

    public double getHeight()
    {
        return pidMotor.getPosition();
    }

    public boolean isLowerLimitSwitchPressed()
    {
        return lowerLimitSwitch.isActive();
    }

    public boolean isUpperLimitSwitchPressed()
    {
        return upperLimitSwitch.isActive();
    }

    public void displayDebugInfo(int lineNum)
    {
        pidController.displayPidInfo(lineNum);
    }

    //
    // Implements TrcPidController.PidInput.
    //
    public double getInput(TrcPidController pidCtrl)
    {
        double value = 0.0;

        if (pidCtrl == pidController)
        {
            value = getHeight();
        }

        return value;
    }   //getInput

    //
    // Implements TrcMotorLimitSwitches.
    //

    public boolean isForwardLimitSwitchActive(TrcMotorController speedController)
    {
        return upperLimitSwitch.isActive();
    }   //isForwardLimitSwitchActive

    public boolean isReverseLimitSwitchActive(TrcMotorController speedController)
    {
        return lowerLimitSwitch.isActive();
    }   //isReverseLimitSwitchActive

    //
    // Implements TrcDigitalTrigger.TriggerHandler
    //

    public void DigitalTriggerEvent(TrcDigitalTrigger digitalTrigger, boolean active)
    {
        if (digitalTrigger == lowerLimitTrigger)
        {
            motor.resetPosition();
        }
    }   //DigitalTriggerEvent

}   //class Elevator
