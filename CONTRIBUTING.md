# Contributing to Progr3SS Habit Tracker

Thank you for your interest in contributing to Progr3SS Habit Tracker! This document provides guidelines for contributing to the project.

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/YOUR_USERNAME/AndroidProjectHabitTracker.git`
3. Create a new branch: `git checkout -b feature/your-feature-name`
4. Make your changes
5. Test your changes thoroughly
6. Commit your changes: `git commit -m "Add: your feature description"`
7. Push to your fork: `git push origin feature/your-feature-name`
8. Create a Pull Request

## Development Guidelines

### Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused (Single Responsibility Principle)
- Add comments for complex logic
- Use proper indentation (4 spaces)

### Architecture Principles

- Maintain MVVM architecture separation
- Keep ViewModels free of Android framework dependencies
- Use repositories as single source of truth
- Implement proper error handling
- Use Resource wrapper for API responses

### Compose Best Practices

- Keep composables small and reusable
- Use remember for state management
- Implement proper hoisting when needed
- Preview your composables
- Use Material 3 components

### Testing

- Write unit tests for ViewModels and Repositories
- Add instrumentation tests for database operations
- Test UI components with Compose test framework
- Ensure all tests pass before submitting PR

## Project Structure

When adding new features, maintain the existing structure:

```
data/
  - local/     # Room database entities and DAOs
  - remote/    # API services and DTOs
  - repository/ # Repository implementations

domain/
  - model/     # Domain models (business logic)

ui/
  - screens/   # Compose screens
  - viewmodel/ # ViewModels
  - theme/     # Theme configuration
  - navigation/ # Navigation setup
```

## Commit Message Guidelines

Use clear, descriptive commit messages:

- `Add: new feature description`
- `Fix: bug description`
- `Update: modification description`
- `Refactor: refactoring description`
- `Test: test description`

## Pull Request Guidelines

### Before Submitting

- [ ] Code follows project style guidelines
- [ ] All tests pass
- [ ] New code has appropriate tests
- [ ] Documentation is updated if needed
- [ ] No unnecessary files are included
- [ ] Commit messages are clear and descriptive

### PR Description

Include:
1. What changes were made
2. Why these changes were necessary
3. How to test the changes
4. Screenshots (for UI changes)
5. Related issue numbers

## Reporting Issues

When reporting issues, please include:

- Android version
- Device model
- Steps to reproduce
- Expected behavior
- Actual behavior
- Screenshots or logs if applicable

## Feature Requests

For feature requests:

1. Check if the feature has already been requested
2. Clearly describe the feature
3. Explain why it would be useful
4. Provide examples if possible

## Code Review Process

All submissions require review. We'll:

1. Review code quality and adherence to guidelines
2. Test functionality
3. Provide constructive feedback
4. Approve or request changes

## Areas for Contribution

### High Priority
- Unit tests for ViewModels
- Integration tests for repositories
- UI tests for critical flows
- Documentation improvements

### Features
- Google Sign-In integration
- Push notifications
- Habit statistics and charts
- Export/import functionality
- Dark theme support

### Improvements
- Performance optimizations
- Accessibility enhancements
- Error handling improvements
- Better offline support

## Questions?

Feel free to open an issue for questions about:
- How to implement a feature
- Clarification on architecture decisions
- Help with development setup

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

Thank you for contributing to Progr3SS Habit Tracker! 🎉
