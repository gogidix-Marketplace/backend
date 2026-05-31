# Fix executive-approval-workflow-service (workflow)
echo "Fixing executive-approval-workflow-service..."
cd "executive-approval-workflow-service"
find src -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.workflow\.domain\.model\.WorkflowDto/com.gogidix.management.executive.workflow.application.dto.WorkflowDto/g' {} \;
find src -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.workflow\.domain\.model\.WorkflowRepository/com.gogidix.management.executive.workflow.domain.repository.WorkflowRepository/g' {} \;
find src -name "*.java" -exec sed -i 's/dashboardId/workflowId/g; s/dashboard:/workflow:/g' {} \;
cd ..

# Fix cto-technology-oversight-service (technology)
echo "Fixing cto-technology-oversight-service..."
cd "cto-technology-oversight-service"
find src -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.technology\.oversight/com.gogidix.management.executive.technology/g' {} \;
find src -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.technology\.domain\.model\.TechnologyDto/com.gogidix.management.executive.technology.application.dto.TechnologyDto/g' {} \;
find src -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.technology\.domain\.model\.TechnologyRepository/com.gogidix.management.executive.technology.domain.repository.TechnologyRepository/g' {} \;
find src -name "*.java" -exec sed -i 's/dashboardId/technologyId/g; s/dashboard:/technology:/g; s/dashboards/technologies/g' {} \;
cd ..

echo "Done fixing!"
