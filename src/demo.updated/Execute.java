package demo.updated;

 
import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.provider.DEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.scheduling.MultithreadedScheduler;
import cz.cuni.mff.d3s.deeco.scheduling.Scheduler;

public class Execute {

	public static void main(String[] args) {
		List<Class<?>> components = Arrays.asList(new Class<?>[] {
		Leader.class,
		FireFighter.class,
		Environment.class,
		Helicopter1.class,
		Helicopter2.class
		});
		
		List<Class<?>> ensembles = Arrays.asList(new Class<?>[] {
		LeaderEnvEnsemble.class,
		FireFighterEnvEnsemble.class,
		Helicopter1EnvEnsemble.class,
		Helicopter2EnvEnsemble.class,
		FireFighterLeaderEnsemble.class,
		FireFighterHelicopterEnsemble.class,
		HelicopterLeaderEnsemble.class,
		HelicopterHelicopterEnsemble.class
 		});
		
		KnowledgeManager km = new RepositoryKnowledgeManager(new LocalKnowledgeRepository());
		Scheduler scheduler = new MultithreadedScheduler();
		
		Runtime rt = new Runtime(km, scheduler);
		System.out.println("comp "+ components.size() + " , ensembles : "+ ensembles.size());
		DEECoObjectProvider provider = new DEECoObjectProvider(components, ensembles);
		System.out.println(" provider " + provider.getEnsembles());
		
		rt.registerComponentsAndEnsembles(provider);
		rt.startRuntime();
	}

}