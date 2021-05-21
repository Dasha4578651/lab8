import java.util.HashSet;

/** 
 * Синглтон для класса Crawler
 * Зачем - да просто так 
 */
public class Crawler 
{
    private static Crawler instance = null; // На старте программы у нас нет экземпляра Crawler

    private WorkerHandler workerHandler;
    private URLPool urlPool;
    private int maxDepth;               // Максимальная глубина обхода для Crawler
    
    /* Это функция для паттерна Синглтон - в прогремме может быть только один Crawler. */
    /* Вручную мы его создать не можем - конструктор private, а значит, только получить ссылку на существующий. */
    public static Crawler getCrawler(String firstUrl, int depth)
    {
        if(instance == null)
            instance = new Crawler(firstUrl, depth);
        return instance;
    }
    private Crawler(String firstUrl, int depth)
    {
        this.maxDepth = depth;                  // устанавливаем максимальную глубину
        this.urlPool = new URLPool();
        this.workerHandler = new WorkerHandler(this.urlPool, maxDepth);

        // Из-за особенности функции, создаём сначала HashSet, добавляем ссылку, а затем передаём этот HashSet функции
        HashSet<URLPair> tempHashSet = new HashSet<>();
        tempHashSet.add(new URLPair(firstUrl, 0));
        this.urlPool.insertToQueue(tempHashSet);  // Добавляем первую ссылку в очередь для обработки
    }

    public void startCrawl()
    {
        this.workerHandler.startTask();
        while(this.workerHandler.isEnd() == false) { try{Thread.sleep(1000);}catch(Exception e){}}
        this.workerHandler.stopTasks();
    }
    public HashSet<URLPair> getVisited()
    {
        return this.urlPool.getVisited();
    }
}