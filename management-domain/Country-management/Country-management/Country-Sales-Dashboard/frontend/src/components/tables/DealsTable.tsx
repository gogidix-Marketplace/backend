import { Deal } from "@/types";
import { formatCurrency, formatDate } from "@/lib/utils";
import { Badge } from "@/components/ui/badge";

interface DealsTableProps {
  deals: Deal[];
}

const stageColors: Record<string, string> = {
  PROSPECTING: "bg-slate-100 text-slate-800 dark:bg-slate-800 dark:text-slate-200",
  QUALIFICATION: "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200",
  NEED_ANALYSIS: "bg-cyan-100 text-cyan-800 dark:bg-cyan-900 dark:text-cyan-200",
  VALUE_PROPOSITION: "bg-teal-100 text-teal-800 dark:bg-teal-900 dark:text-teal-200",
  PROPOSAL: "bg-amber-100 text-amber-800 dark:bg-amber-900 dark:text-amber-200",
  NEGOTIATION: "bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200",
  CLOSED_WON: "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200",
  CLOSED_LOST: "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200",
};

const priorityColors: Record<string, string> = {
  LOW: "bg-slate-100 text-slate-600",
  MEDIUM: "bg-blue-100 text-blue-600",
  HIGH: "bg-orange-100 text-orange-600",
  URGENT: "bg-red-100 text-red-600",
};

export default function DealsTable({ deals }: DealsTableProps) {
  if (deals.length === 0) {
    return (
      <div className="text-center py-8 text-muted-foreground">No deals found</div>
    );
  }

  return (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr className="border-b">
            <th className="text-left py-3 px-4 font-medium text-sm text-muted-foreground">Deal</th>
            <th className="text-left py-3 px-4 font-medium text-sm text-muted-foreground">Customer</th>
            <th className="text-left py-3 px-4 font-medium text-sm text-muted-foreground">Stage</th>
            <th className="text-left py-3 px-4 font-medium text-sm text-muted-foreground">Value</th>
            <th className="text-left py-3 px-4 font-medium text-sm text-muted-foreground">Priority</th>
            <th className="text-left py-3 px-4 font-medium text-sm text-muted-foreground">Close Date</th>
            <th className="text-left py-3 px-4 font-medium text-sm text-muted-foreground">Owner</th>
          </tr>
        </thead>
        <tbody>
          {deals.map((deal) => (
            <tr key={deal.id} className="border-b hover:bg-accent/50 transition-colors">
              <td className="py-3 px-4">
                <div>
                  <div className="font-medium">{deal.title}</div>
                  <div className="text-xs text-muted-foreground">{deal.dealCode}</div>
                </div>
              </td>
              <td className="py-3 px-4 text-sm">{deal.customerName}</td>
              <td className="py-3 px-4">
                <Badge className={stageColors[deal.stage] || stageColors.PROSPECTING}>
                  {deal.stage.replace(/_/g, " ")}
                </Badge>
              </td>
              <td className="py-3 px-4 text-sm font-medium">{formatCurrency(deal.value)}</td>
              <td className="py-3 px-4">
                <Badge className={priorityColors[deal.priority] || priorityColors.MEDIUM}>
                  {deal.priority}
                </Badge>
              </td>
              <td className="py-3 px-4 text-sm">
                <span className={deal.overdue ? "text-red-600 font-medium" : ""}>
                  {formatDate(deal.expectedCloseDate)}
                </span>
              </td>
              <td className="py-3 px-4 text-sm text-muted-foreground">{deal.ownerName || "Unassigned"}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
