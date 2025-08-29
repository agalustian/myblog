1. Implement `equals` method for Models;
2. Use `text[]` for tags;
3. Create unique constraints for tables (posts, ...etc);
4. Add `checkstyle`, `pmd`;
5. Add `pre-commit hook`;
6. Add code formatter in `pre-commit hook`;
7. Use user data from headers instead hardcoded `CURRENT_USER`;
8. Edit comment submit bug ctrl+enter send form bug;
9. Hidden tag filter after searching unknown tag;
10. `@RequestParam("userId") String userId` - rework on authorization implementation. Remove `?userId=CURRENT_USER` from templates