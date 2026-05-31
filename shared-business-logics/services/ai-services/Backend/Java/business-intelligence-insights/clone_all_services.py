#!/usr/bin/env python3
"""
AI Services Cloning Script
Clones 42 AI microservices from the ai-customer-segmentation-service blueprint.
"""

import os
import re
import shutil
import subprocess
import time
from pathlib import Path
from datetime import datetime
from typing import Dict, List, Tuple

# Configuration
BLUEPRINT_DIR = Path("ai-customer-segmentation-service")
SERVICES_DIR = Path.cwd()
LOG_FILE = SERVICES_DIR / "CLONING_LOG.md"

# 42 Services to clone
SERVICES = [
    # BATCH 1 - Core Customer Services (5 services)
    ("1", "ai-product-recommendation-service", "HIGH", "Product", "ProductRecommendation"),
    ("2", "ai-loyalty-program-service", "HIGH", "LoyaltyProgram", "LoyaltyTier"),
    ("3", "ai-customer-churn-prediction-service", "HIGH", "ChurnPrediction", "ChurnFactor"),
    ("4", "ai-customer-ltv-service", "HIGH", "CustomerLTV", "LTVCalculation"),
    ("5", "ai-customer-insight-service", "HIGH", "CustomerInsight", "CustomerPreference"),

    # BATCH 2 - Segmentation & Analytics (5 services)
    ("6", "ai-demographic-segmentation-service", "HIGH", "DemographicSegment", "DemographicCriteria"),
    ("7", "ai-behavioral-segmentation-service", "HIGH", "BehavioralSegment", "BehaviorPattern"),
    ("8", "ai-psychographic-service", "MEDIUM", "PsychographicProfile", "PsychographicTrait"),
    ("9", "ai-transaction-segmentation-service", "MEDIUM", "TransactionSegment", "TransactionPattern"),
    ("10", "ai-customer-analytics-service", "HIGH", "CustomerAnalytics", "AnalyticsMetric"),

    # BATCH 3 - Marketing & Campaign (5 services)
    ("11", "ai-campaign-optimization-service", "MEDIUM", "Campaign", "CampaignRule"),
    ("12", "ai-content-optimization-service", "MEDIUM", "ContentItem", "ContentRule"),
    ("13", "ai-offer-optimization-service", "HIGH", "Offer", "OfferRule"),
    ("14", "ai-recommendation-engine-service", "HIGH", "Recommendation", "RecommendationRule"),
    ("15", "ai-personalization-service", "MEDIUM", "PersonalizationRule", "PersonalizedContent"),

    # BATCH 4 - Commerce & Sales (5 services)
    ("16", "ai-demand-forecasting-service", "HIGH", "DemandForecast", "ForecastResult"),
    ("17", "ai-inventory-optimization-service", "HIGH", "Inventory", "StockLevel"),
    ("18", "ai-price-optimization-service", "HIGH", "Price", "PriceRule"),
    ("19", "ai-market-basket-service", "MEDIUM", "MarketBasket", "BasketItem"),
    ("20", "ai-cross-sell-service", "MEDIUM", "CrossSellOffer", "CrossSellRule"),

    # BATCH 5 - Customer Journey (5 services)
    ("21", "ai-upsell-service", "MEDIUM", "UpsellOffer", "UpsellRule"),
    ("22", "ai-retention-service", "HIGH", "RetentionRule", "RetentionCampaign"),
    ("23", "ai-affinity-service", "LOW", "AffinityGroup", "AffinityRule"),
    ("24", "ai-journey-service", "MEDIUM", "CustomerJourney", "JourneyStage"),
    ("25", "ai-next-best-action-service", "MEDIUM", "NextBestAction", "ActionRecommendation"),

    # BATCH 6 - Sentiment & Search (5 services)
    ("26", "ai-sentiment-analysis-service", "MEDIUM", "SentimentAnalysis", "SentimentScore"),
    ("27", "ai-emotion-service", "LOW", "EmotionAnalysis", "EmotionScore"),
    ("28", "ai-competitor-analysis-service", "MEDIUM", "Competitor", "CompetitorPrice"),
    ("29", "ai-search-relevance-service", "MEDIUM", "SearchRelevance", "RankingAlgorithm"),
    ("30", "ai-lookalike-service", "LOW", "LookalikeProfile", "SimilarityScore"),

    # BATCH 7 - Advanced Analytics (5 services)
    ("31", "ai-lead-scoring-service", "HIGH", "LeadScore", "LeadFactor"),
    ("32", "ai-propensity-service", "MEDIUM", "PropensityModel", "PropensityScore"),
    ("33", "ai-attribution-service", "HIGH", "AttributionModel", "AttributionFactor"),
    ("34", "ai-virality-service", "LOW", "ViralityCoefficient", "ViralMetric"),
    ("35", "ai-assortment-service", "MEDIUM", "Assortment", "AssortmentRule"),

    # BATCH 8 - Platform Services (7 services)
    ("36", "ai-channel-optimization-service", "MEDIUM", "Channel", "ChannelMetric"),
    ("37", "ai-revenue-optimization-service", "HIGH", "Revenue", "RevenueMetric"),
    ("38", "ai-qos-service", "LOW", "QoS", "ServiceLevel"),
    ("39", "ai-price-elasticity-service", "MEDIUM", "PriceElasticity", "ElasticityModel"),
    ("40", "ai-fraud-detection-service", "HIGH", "FraudPattern", "FraudScore"),
    ("41", "ai-sla-service", "HIGH", "SLA", "ServiceLevelAgreement"),
    ("42", "ai-transaction-monitoring-service", "HIGH", "TransactionMonitor", "MonitoringRule"),
]

# Domain mappings for replacements
DOMAIN_REPLACEMENTS: Dict[str, List[Tuple[str, str]]] = {
    "ai-product-recommendation-service": [
        ("CustomerSegment", "Product"),
        ("SegmentCriteria", "ProductCriteria"),
        ("CustomerProfile", "UserProfile"),
        ("Segment", "Product"),
    ],
    "ai-loyalty-program-service": [
        ("CustomerSegment", "LoyaltyProgram"),
        ("SegmentCriteria", "LoyaltyTier"),
        ("CustomerProfile", "MemberProfile"),
        ("Segment", "LoyaltyProgram"),
    ],
    "ai-customer-churn-prediction-service": [
        ("CustomerSegment", "ChurnPrediction"),
        ("SegmentCriteria", "ChurnFactor"),
        ("Segment", "ChurnPrediction"),
    ],
    "ai-customer-ltv-service": [
        ("CustomerSegment", "CustomerLTV"),
        ("SegmentCriteria", "LTVCalculation"),
        ("Segment", "LTV"),
    ],
    "ai-customer-insight-service": [
        ("CustomerSegment", "CustomerInsight"),
        ("SegmentCriteria", "InsightCriteria"),
        ("Segment", "Insight"),
    ],
    # Default fallback for others
}


def get_package_name(service_name: str) -> str:
    """Convert service name to package name."""
    # Remove 'ai-' prefix and '-service' suffix, convert to camelCase
    name = service_name.replace("ai-", "").replace("-service", "")
    # Convert kebab-case to camelCase
    parts = name.split("-")
    return "ai" + "".join(p.capitalize() for p in parts) + "service"


def get_class_name(service_name: str) -> str:
    """Convert service name to main class name."""
    name = service_name.replace("ai-", "").replace("-service", "")
    parts = name.split("-")
    return "AI" + "".join(p.capitalize() for p in parts) + "ServiceApplication"


def get_db_name(service_name: str) -> str:
    """Convert service name to database name."""
    name = service_name.replace("ai-", "").replace("-service", "")
    return f"ai_{name}_db"


def replace_in_file(file_path: Path, replacements: List[Tuple[str, str]]) -> None:
    """Replace content in a file."""
    if not file_path.exists():
        return

    content = file_path.read_text(encoding="utf-8")
    for old, new in replacements:
        content = content.replace(old, new)
    file_path.write_text(content, encoding="utf-8")


def rename_file(file_path: Path, old_name: str, new_name: str) -> Path:
    """Rename a file if it contains the old name in its filename."""
    if old_name in file_path.name:
        new_path = file_path.with_name(file_path.name.replace(old_name, new_name))
        file_path.rename(new_path)
        return new_path
    return file_path


def rename_directory_recursively(dir_path: Path, old_name: str, new_name: str) -> Path:
    """Rename directories recursively from bottom to top."""
    # First, rename all files and subdirectories
    for item in sorted(dir_path.rglob("*"), key=lambda x: len(x.parts), reverse=True):
        if item.is_dir() and old_name in item.name:
            new_item = item.with_name(item.name.replace(old_name, new_name))
            if not new_item.exists():
                item.rename(new_item)
        elif item.is_file():
            if old_name in item.name:
                new_item = item.with_name(item.name.replace(old_name, new_name))
                if not new_item.exists():
                    item.rename(new_item)

    return dir_path


def clone_service(service_num: str, service_name: str, priority: str,
                 domain1: str, domain2: str) -> Dict:
    """Clone a single service from blueprint."""
    result = {
        "service": service_name,
        "status": "STARTED",
        "start_time": datetime.now().isoformat(),
        "end_time": None,
        "errors": [],
        "tests_passed": 0,
        "tests_total": 0,
    }

    print(f"\n{'='*60}")
    print(f"CLONING SERVICE: {service_name}")
    print(f"NUMBER: {service_num} | PRIORITY: {priority}")
    print(f"DOMAIN: {domain1}, {domain2}")
    print(f"STARTED: {result['start_time']}")
    print(f"{'='*60}")

    try:
        # Step 1: Copy blueprint directory
        print("Step 1: Copying blueprint...")
        new_service_dir = SERVICES_DIR / service_name

        if new_service_dir.exists():
            print(f"  Service directory already exists, removing...")
            def on_rm_error(func, path, exc_info):
                os.chmod(path, 0o777)
                func(path)
            shutil.rmtree(new_service_dir, onerror=on_rm_error)

        shutil.copytree(BLUEPRINT_DIR, new_service_dir)
        print(f"  Created: {new_service_dir}")

        # Get package and class names
        old_pkg = "aicustomersegmentationservice"
        old_pkg_short = "aicustomersegmentation"
        new_pkg = get_package_name(service_name)
        new_pkg_short = new_pkg.replace("service", "")
        old_class = "AiCustomerSegmentationServiceApplication"
        new_class = get_class_name(service_name)
        old_db = "ai_customer_segmentation_db"
        new_db = get_db_name(service_name)

        # Step 2: Rename directories first (bottom to top)
        print("Step 2: Renaming directories...")
        rename_directory_recursively(new_service_dir, old_pkg, new_pkg)
        if (new_service_dir / "src" / "main" / "java" / "com" / "gogidix" / "aiservices" / old_pkg_short).exists():
            old_short_path = new_service_dir / "src" / "main" / "java" / "com" / "gogidix" / "aiservices" / old_pkg_short
            new_short_path = new_service_dir / "src" / "main" / "java" / "com" / "gogidix" / "aiservices" / new_pkg_short
            if old_short_path != new_short_path:
                shutil.move(old_short_path, new_short_path)

        # Step 3: Update all Java files
        print("Step 3: Updating Java files...")
        java_files = list(new_service_dir.rglob("*.java"))

        # Get domain-specific replacements
        domain_replacements = DOMAIN_REPLACEMENTS.get(service_name, [])

        for java_file in java_files:
            # Build replacements list
            replacements = [
                (f"package com.gogidix.aiservices.{old_pkg};",
                 f"package com.gogidix.aiservices.{new_pkg};"),
                (f"package com.gogidix.aiservices.{old_pkg_short};",
                 f"package com.gogidix.aiservices.{new_pkg_short};"),
                (f"import com.gogidix.aiservices.{old_pkg}.",
                 f"import com.gogidix.aiservices.{new_pkg}."),
                (f"import com.gogidix.aiservices.{old_pkg_short}.",
                 f"import com.gogidix.aiservices.{new_pkg_short}."),
                (old_class, new_class),
                ("AI Customer Segmentation Service",
                 f"AI {' '.join(word.capitalize() for word in service_name.replace('ai-', '').replace('-service', '').split('-'))} Service"),
                ("ai-customer-segmentation-service", service_name),
                ("ai_customer_segmentation_db", new_db),
            ]

            # Add domain-specific replacements
            for old_domain, new_domain in domain_replacements:
                replacements.append((old_domain, new_domain))

            replace_in_file(java_file, replacements)

        # Step 4: Update XML and YAML files
        print("Step 4: Updating configuration files...")
        config_files = list(new_service_dir.rglob("*.xml")) + list(new_service_dir.rglob("*.yml")) + list(new_service_dir.rglob("*.yaml"))

        for config_file in config_files:
            replacements = [
                ("ai-customer-segmentation-service", service_name),
                ("aicustomersegmentationservice", new_pkg),
                ("aicustomersegmentation", new_pkg_short),
                (old_class, new_class),
                ("ai_customer_segmentation_db", new_db),
                ("AI Customer Segmentation Service",
                 f"AI {' '.join(word.capitalize() for word in service_name.replace('ai-', '').replace('-service', '').split('-'))} Service"),
            ]
            replace_in_file(config_file, replacements)

        # Step 5: Update README, Dockerfile, and other text files
        print("Step 5: Updating documentation...")
        for txt_file in new_service_dir.rglob("README.md"):
            service_display = " ".join(word.capitalize() for word in service_name.replace("ai-", "").replace("-service", "").split("-"))
            replacements = [
                ("AI Customer Segmentation Service", f"AI {service_display} Service"),
                ("ai-customer-segmentation-service", service_name),
                ("customer segment", service_name.replace("ai-", "").replace("-service", "") + " segment"),
                ("CustomerSegment", domain1 if domain1 else "Entity"),
            ]
            replace_in_file(txt_file, replacements)

        # Step 6: Update Dockerfile
        dockerfile = new_service_dir / "Dockerfile"
        if dockerfile.exists():
            replacements = [
                ("ai-customer-segmentation-service", service_name),
                (old_class, new_class),
            ]
            replace_in_file(dockerfile, replacements)

        # Step 7: Update K8s manifests
        for k8s_file in new_service_dir.rglob("*.yaml"):
            if "k8s" in str(k8s_file):
                replacements = [
                    ("ai-customer-segmentation-service", service_name),
                    ("ai-customer-segmentation", service_name),
                ]
                replace_in_file(k8s_file, replacements)

        # Step 8: Clean up marker files and logs
        for marker in new_service_dir.rglob("*.marker"):
            marker.unlink()
        for log_file in new_service_dir.rglob("hs_err*.log"):
            log_file.unlink()

        # Step 9: Compile test
        print("Step 6: Running Maven compile...")
        compile_result = subprocess.run(
            ["mvn", "clean", "compile", "-q"],
            cwd=new_service_dir,
            capture_output=True,
            text=True,
            timeout=300
        )

        if compile_result.returncode != 0:
            result["status"] = "COMPILATION_FAILED"
            result["errors"].append(f"Compilation failed: {compile_result.stderr[-500:]}")
            print(f"  ERROR: Compilation failed")
            return result

        print("  Compilation: OK")

        # Step 10: Run tests
        print("Step 7: Running Maven test...")
        test_result = subprocess.run(
            ["mvn", "test", "-q", "-Djacoco.skip=true"],
            cwd=new_service_dir,
            capture_output=True,
            text=True,
            timeout=300
        )

        # Parse test results
        test_output = test_result.stdout + test_result.stderr
        tests_match = re.search(r'Tests run: (\d+), Failures: (\d+), Errors: (\d+)', test_output)
        if tests_match:
            result["tests_total"] = int(tests_match.group(1))
            result["tests_passed"] = int(tests_match.group(1)) - int(tests_match.group(2)) - int(tests_match.group(3))

        if test_result.returncode != 0:
            result["status"] = "TESTS_FAILED"
            result["errors"].append(f"Tests failed: {test_output[-500:]}")
            print(f"  ERROR: Tests failed")
            return result

        print(f"  Tests: {result['tests_passed']}/{result['tests_total']} passed")

        # Step 11: Package
        print("Step 8: Creating JAR...")
        package_result = subprocess.run(
            ["mvn", "package", "-DskipTests", "-q"],
            cwd=new_service_dir,
            capture_output=True,
            text=True,
            timeout=300
        )

        if package_result.returncode != 0:
            result["status"] = "PACKAGE_FAILED"
            result["errors"].append(f"Package failed: {package_result.stderr[-500:]}")
            print(f"  ERROR: Package failed")
            return result

        print("  JAR created: OK")

        # Success!
        result["status"] = "DONE"
        result["end_time"] = datetime.now().isoformat()

    except Exception as e:
        result["status"] = "ERROR"
        result["errors"].append(str(e))
        result["end_time"] = datetime.now().isoformat()
        print(f"  ERROR: {e}")

    return result


def main():
    """Main execution function."""
    print("\n" + "="*60)
    print("     AI SERVICES RAPID CLONING EXECUTION")
    print(f"     Target: {len(SERVICES)} services")
    print("="*60)

    # Initialize log file
    with open(LOG_FILE, "w") as f:
        f.write(f"# AI Services Cloning Log\n")
        f.write(f"Started: {datetime.now().isoformat()}\n")
        f.write(f"Total Services: {len(SERVICES)}\n\n")

    completed = 0
    failed = 0
    results = []

    # Change to business-intelligence-insights directory
    # Get current working directory - we should already be in the right place
    bi_dir = Path.cwd()
    print(f"Working directory: {bi_dir}")

    for service_num, service_name, priority, domain1, domain2 in SERVICES:
        result = clone_service(service_num, service_name, priority, domain1, domain2)
        results.append(result)

        # Update log
        with open(LOG_FILE, "a") as f:
            f.write(f"## {service_name}\n")
            f.write(f"- Status: {result['status']}\n")
            f.write(f"- Started: {result['start_time']}\n")
            if result['end_time']:
                f.write(f"- Completed: {result['end_time']}\n")
            if result['tests_total'] > 0:
                f.write(f"- Tests: {result['tests_passed']}/{result['tests_total']}\n")
            if result['errors']:
                f.write(f"- Errors: {len(result['errors'])}\n")
                for err in result['errors'][:3]:
                    f.write(f"  - {err[:200]}...\n")
            f.write("\n")

        if result["status"] == "DONE":
            completed += 1
        else:
            failed += 1

        # Brief pause between services
        time.sleep(1)

    # Print summary
    print("\n" + "="*60)
    print("                    CLONING SUMMARY")
    print("="*60)
    print(f"Completed: {completed}/{len(SERVICES)} services")
    print(f"Failed: {failed}/{len(SERVICES)} services")
    if completed > 0:
        print(f"Success Rate: {completed * 100 // len(SERVICES)}%")
    print("="*60)

    # Update log with summary
    with open(LOG_FILE, "a") as f:
        f.write("\n## SUMMARY\n")
        f.write(f"- Completed: {completed}/{len(SERVICES)}\n")
        f.write(f"- Failed: {failed}/{len(SERVICES)}\n")
        f.write(f"- Success Rate: {completed * 100 // len(SERVICES) if completed > 0 else 0}%\n")
        f.write(f"- Ended: {datetime.now().isoformat()}\n")

    if completed == len(SERVICES):
        print("\nALL 42 SERVICES CLONED SUCCESSFULLY!")
        return 0
    else:
        print(f"\n{failed} services failed. Check {LOG_FILE}")
        return 1


if __name__ == "__main__":
    exit(main())
