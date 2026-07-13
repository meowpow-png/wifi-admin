The frontend is implemented using a modern React-based technology stack selected for simplicity, maintainability, and alignment with current industry practices.

| Technology            | Purpose                                         |
|-----------------------|-------------------------------------------------|
| React                 | Component-based user interface                  |
| TypeScript            | Static typing and improved developer experience |
| Vite                  | Development server and production build tooling |
| React Router          | Client-side routing                             |
| CSS Modules           | Component-scoped styling                        |
| Fetch API             | Communication with the backend REST API         |
| React Hooks           | Component state and application logic           |
| Lucide React          | Icon library                                    |
| React Hot Toast       | User notifications                              |
| Vitest                | Unit testing                                    |
| React Testing Library | Component testing                               |
| ESLint                | Static code analysis                            |
| Prettier              | Code formatting                                 |

The application intentionally avoids additional state management, form, and UI component libraries. Given the limited scope of the application, React state, custom hooks, and controlled form components provide a straightforward solution while keeping the dependency footprint small.

HTTP communication is implemented using the browser's native Fetch API. All network interactions are encapsulated within feature API modules, allowing UI components and application logic to remain independent of HTTP concerns.

Styling is implemented using CSS Modules to provide locally scoped styles without introducing a global CSS framework. This keeps components self-contained while avoiding unnecessary complexity.

The frontend is built and bundled using Vite, which provides a fast development environment and an efficient production build suitable for modern React applications.

Quality is maintained through ESLint and Prettier for consistent code style, while Vitest and React Testing Library provide automated testing for application logic and user interface components.
