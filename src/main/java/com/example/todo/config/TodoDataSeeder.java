package com.example.todo.config;

import com.example.todo.enums.Priority;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class TodoDataSeeder implements CommandLineRunner {
    private final TodoRepository todoRepository;

    public TodoDataSeeder(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!todoRepository.findAll().isEmpty()) return;

        List<Todo> newTodos = List.of(
                new Todo("Alpha project planning", Priority.MEDIUM),
                new Todo("Buy groceries", Priority.LOW),
                new Todo("Complete report", Priority.HIGH),
                new Todo("Draft email to team", Priority.MEDIUM),
                new Todo("Exercise for 30 minutes", Priority.MEDIUM),
                new Todo("Fix broken pipeline", Priority.HIGH),
                new Todo("Gather feedback from stakeholders", Priority.MEDIUM),
                new Todo("Host meeting", Priority.MEDIUM),
                new Todo("Improve UI design", Priority.LOW),
                new Todo("Join coding workshop", Priority.MEDIUM),
                new Todo("Kickoff new marketing campaign", Priority.HIGH),
                new Todo("Learn Spring Boot basics", Priority.LOW),
                new Todo("Manage team assignment", Priority.MEDIUM),
                new Todo("Notify users about changes", Priority.MEDIUM),
                new Todo("Organize desk space", Priority.LOW),
                new Todo("Plan upcoming sprints", Priority.MEDIUM),
                new Todo("Quick bug fix", Priority.HIGH),
                new Todo("Review pull request", Priority.MEDIUM),
                new Todo("Schedule team outing", Priority.LOW),
                new Todo("Test new feature deployment", Priority.HIGH),
                new Todo("Update documentation", Priority.MEDIUM),
                new Todo("Write monthly newsletter", Priority.LOW),
                new Todo("Xerox meeting agenda", Priority.LOW),
                new Todo("Verify backup status", Priority.MEDIUM),
                new Todo("Create presentation slides", Priority.MEDIUM),
                new Todo("Deploy production build", Priority.HIGH),
                new Todo("Test performance optimization", Priority.HIGH),
                new Todo("Read security guidelines", Priority.MEDIUM),
                new Todo("Check email for client updates", Priority.LOW),
                new Todo("Update team on project status", Priority.MEDIUM),
                new Todo("Review recent bug reports", Priority.HIGH),
                new Todo("Prepare for upcoming conference", Priority.MEDIUM),
                new Todo("Check server performance metrics", Priority.LOW),
                new Todo("Compile data for analysis", Priority.MEDIUM),
                new Todo("Review design documents", Priority.MEDIUM),
                new Todo("Create backup for project", Priority.HIGH),
                new Todo("Research new development tools", Priority.LOW),
                new Todo("Organize virtual team meeting", Priority.MEDIUM),
                new Todo("Finalize budget for next quarter", Priority.HIGH),
                new Todo("Configure database backups", Priority.HIGH),
                new Todo("Fix UI bug in the application", Priority.MEDIUM),
                new Todo("Submit tax documents", Priority.HIGH),
                new Todo("Write blog post for company website", Priority.LOW),
                new Todo("Review contract terms", Priority.MEDIUM),
                new Todo("Send out invoices", Priority.LOW),
                new Todo("Plan budget for upcoming project", Priority.MEDIUM),
                new Todo("Research competitors' product features", Priority.LOW),
                new Todo("Test bug fixes in development branch", Priority.HIGH),
                new Todo("Host team-building event", Priority.MEDIUM),
                new Todo("Complete UI redesign", Priority.HIGH),
                new Todo("Audit recent code changes", Priority.MEDIUM),
                new Todo("Send reminder email for meeting", Priority.LOW),
                new Todo("Update project roadmap", Priority.MEDIUM),
                new Todo("Fix security vulnerability", Priority.HIGH),
                new Todo("Schedule lunch with the team", Priority.LOW),
                new Todo("Monitor website traffic", Priority.LOW),
                new Todo("Check system performance", Priority.MEDIUM),
                new Todo("Finalize content for product launch", Priority.HIGH),
                new Todo("Set up new development environment", Priority.MEDIUM),
                new Todo("Plan holiday break", Priority.LOW),
                new Todo("Prepare meeting agenda", Priority.MEDIUM),
                new Todo("Test mobile app performance", Priority.HIGH),
                new Todo("Write feature request", Priority.LOW),
                new Todo("Design system architecture", Priority.MEDIUM),
                new Todo("Prepare demo for client presentation", Priority.HIGH),
                new Todo("Attend project management workshop", Priority.LOW),
                new Todo("Optimize code performance", Priority.HIGH),
                new Todo("Conduct code review for team", Priority.MEDIUM),
                new Todo("Organize team celebration", Priority.LOW),
                new Todo("Perform security audit", Priority.HIGH),
                new Todo("Update application dependencies", Priority.MEDIUM),
                new Todo("Add new feature to product", Priority.HIGH),
                new Todo("Attend industry conference", Priority.LOW),
                new Todo("Check project budget", Priority.MEDIUM),
                new Todo("Meet with client for feedback", Priority.MEDIUM),
                new Todo("Start new marketing campaign", Priority.HIGH),
                new Todo("Plan product roadmap for next quarter", Priority.MEDIUM),
                new Todo("Test new API endpoint", Priority.HIGH),
                new Todo("Set up automated testing", Priority.MEDIUM),
                new Todo("Finalize user stories for sprint", Priority.MEDIUM),
                new Todo("Update code documentation", Priority.MEDIUM),
                new Todo("Draft project proposal", Priority.LOW),
                new Todo("Write user manual", Priority.LOW),
                new Todo("Prepare client onboarding materials", Priority.HIGH),
                new Todo("Resolve database issues", Priority.HIGH),
                new Todo("Test new login functionality", Priority.MEDIUM),
                new Todo("Meet with HR to discuss benefits", Priority.LOW),
                new Todo("Create weekly team progress report", Priority.MEDIUM),
                new Todo("Schedule feedback session with client", Priority.MEDIUM),
                new Todo("Create marketing plan", Priority.MEDIUM),
                new Todo("Finalize client contract", Priority.HIGH),
                new Todo("Review new job applications", Priority.LOW),
                new Todo("Attend team meeting", Priority.MEDIUM),
                new Todo("Complete performance evaluation", Priority.MEDIUM),
                new Todo("Write proposal for new project", Priority.HIGH),
                new Todo("Complete user research", Priority.MEDIUM),
                new Todo("Host lunch for the team", Priority.LOW)
        );

        Random random = new Random();

        newTodos.forEach(todo -> {
            // Randomly assign due dates to some todos
            if (random.nextInt(3) > 0) {
                todo.setDueDate(todo.getCreatedAt()
                        .plusDays(random.nextInt(30))
                        .plusHours(random.nextInt(24))
                        .plusMinutes(random.nextInt(60))
                        .plusSeconds(random.nextInt(60)));
            }

            // Randomly mark some todos as done with done date
            if (random.nextBoolean()) {
                todo.setDone(true);
                todo.setDoneDate(todo.getCreatedAt()
                        .plusDays(random.nextInt(30))
                        .plusHours(random.nextInt(24))
                        .plusMinutes(random.nextInt(60))
                        .plusSeconds(random.nextInt(60)));
            }

            todoRepository.save(todo);
        });

        System.out.println("Seeded initial todos.");
    }
}
