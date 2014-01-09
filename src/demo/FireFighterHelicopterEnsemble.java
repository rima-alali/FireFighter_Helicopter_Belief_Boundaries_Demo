package demo.updated;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.ensemble.Ensemble;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;

public class FireFighterHelicopterEnsemble extends Ensemble {

	@Membership
	public static boolean membership(
			@In("coord.ffPos") Double ffPos,
			@In("coord.ffSpeed") Double ffSpeed,
			@In("coord.ffCreationTime") Double ffCreationTime,
			@In("coord.ffLastCommunication") Double ffLastConn,

			@In("member.hPos") Double hPos,
			@In("member.hRangeDistance") Double hRangeDistance,
			@In("member.hFFConnected") Boolean hFFConnected,
			@In("member.hFFPos") Double hFFPos,
			@In("member.hFFSpeed") Double hFFSpeed,
			@In("member.hFFCreationTime") Double hFFCreationTime) {

		double currentTime = (double) System.nanoTime() / 1000000000;
		boolean conn = hFFConnected == null ? false : hFFConnected;
		double vffLastConn = ffLastConn == null ? 0.0 : ffLastConn;

		// ------------------------------------------------------------------------------------
		boolean inCommunicationRange = (ffPos < 1000 ||(ffPos > 1200 && ffPos < 1600)); //the ranges of communication with two helicopters
		boolean isDelayed = (currentTime - vffLastConn) > 1; // delay
		double dist = (Math.abs(hPos - ffPos));
		boolean inRange = dist < 2*hRangeDistance;
		// ------------------------------------------------------------------------------------
		if (inRange && inCommunicationRange && !conn && isDelayed && vffLastConn != 0.0) {
			System.err.println("###delay :" + (currentTime - vffLastConn)+"  helicopter at pos: " + hPos + "  was asked to connect firefighter pos: "+ffPos+" and its belief is :"+hFFPos+"  . current time = "+ currentTime + "     ffLastCommunication : "+ vffLastConn);
			return true;
		} else if (inRange && inCommunicationRange && conn){
			System.err.println("Helicopter at Pos : "+hPos+" connected to firefighter :"+ffPos);
			return true;
		}
		return false;
	}

	@KnowledgeExchange
	@PeriodicScheduling(50)
	public static void map(
			@In("coord.ffPos") Double ffPos,
			@In("coord.ffSpeed") Double ffSpeed,
			@In("coord.ffCreationTime") Double ffCreationTime,
			@Out("coord.ffLastCommunication") OutWrapper<Double> ffLastConn,

			@Out("member.hFFPos") OutWrapper<Double> hFFPos,
			@Out("member.hFFSpeed") OutWrapper<Double> hFFSpeed,
			@Out("member.hFFConnected") OutWrapper<Boolean> hFFConnected,
			@Out("member.hFFCreationTime") OutWrapper<Double> hFFCreationTime) {

		hFFPos.value = ffPos;
		hFFSpeed.value = ffSpeed;
		hFFCreationTime.value = ffCreationTime;
		hFFConnected.value = true;
		ffLastConn.value = ffCreationTime;
	}
}