# 🧪 Test Coverage Report

_Generated on 2025-09-18_

## 🔢 Overall Summary

- **Line Coverage:** **94.8%** (810 covered / 44 missed)

## 📦 Coverage by Package

### `com.expediagroup.baggageancillaryservice.util`

| Metric | Coverage | Missed | Covered |
|---|---:|---:|---:|
| INSTRUCTION | 87.2% | 129 | 882 |
| BRANCH | 77.5% | 25 | 86 |
| LINE | 90.5% | 28 | 266 |
| COMPLEXITY | 72.5% | 25 | 66 |
| METHOD | 90.0% | 3 | 27 |
| CLASS | 100.0% | 0 | 4 |

### `com.expediagroup.baggageancillaryservice.service.srs.mapper.request`

| Metric | Coverage | Missed | Covered |
|---|---:|---:|---:|
| INSTRUCTION | 100.0% | 0 | 296 |
| BRANCH | 100.0% | 0 | 8 |
| LINE | 100.0% | 0 | 102 |
| COMPLEXITY | 100.0% | 0 | 19 |
| METHOD | 100.0% | 0 | 15 |
| CLASS | 100.0% | 0 | 2 |

### `com.expediagroup.baggageancillaryservice.service.srs.mapper.response`

| Metric | Coverage | Missed | Covered |
|---|---:|---:|---:|
| INSTRUCTION | 91.7% | 15 | 166 |
| BRANCH | 63.6% | 8 | 14 |
| LINE | 98.1% | 1 | 52 |
| COMPLEXITY | 61.1% | 7 | 11 |
| METHOD | 100.0% | 0 | 7 |
| CLASS | 100.0% | 0 | 1 |

### `com.expediagroup.baggageancillaryservice.service.baggageancillarymodel.mapper`

| Metric | Coverage | Missed | Covered |
|---|---:|---:|---:|
| INSTRUCTION | 100.0% | 0 | 367 |
| BRANCH | 100.0% | 0 | 2 |
| LINE | 100.0% | 0 | 118 |
| COMPLEXITY | 100.0% | 0 | 21 |
| METHOD | 100.0% | 0 | 20 |
| CLASS | 100.0% | 0 | 1 |

### `com.expediagroup.baggageancillaryservice.service.baggageancillaryshop.mapper`

| Metric | Coverage | Missed | Covered |
|---|---:|---:|---:|
| INSTRUCTION | 93.2% | 32 | 440 |
| BRANCH | 77.3% | 5 | 17 |
| LINE | 91.7% | 12 | 132 |
| COMPLEXITY | 80.0% | 5 | 20 |
| METHOD | 100.0% | 0 | 14 |
| CLASS | 100.0% | 0 | 1 |

### `com.expediagroup.baggageancillaryservice.service.pad.mapper.request`

| Metric | Coverage | Missed | Covered |
|---|---:|---:|---:|
| INSTRUCTION | 97.8% | 10 | 445 |
| BRANCH | 66.7% | 11 | 22 |
| LINE | 97.9% | 3 | 140 |
| COMPLEXITY | 68.6% | 11 | 24 |
| METHOD | 100.0% | 0 | 18 |
| CLASS | 100.0% | 0 | 5 |

## 🚨 Hotspots: Top Classes Needing Tests

| Class | Line | Branch | Complexity | Missed Lines | Suggested Focus |
|---|---:|---:|---:|---:|---|
| `com.expediagroup.baggageancillaryservice.util.LoggingUtil` | 76.9% | 0.0% | 60.0% | 3 | branches (true/false, switch cases, exceptions) + complex flows (nested logic, loops) + missing lines (core business paths) |
| `com.expediagroup.baggageancillaryservice.service.pad.mapper.request.SegmentFlightCriteriaMapper` | 92.3% | 50.0% | 50.0% | 2 | branches (true/false, switch cases, exceptions) + complex flows (nested logic, loops) |
| `com.expediagroup.baggageancillaryservice.service.pad.mapper.request.AncillaryCriteriaMapper` | 100.0% | 50.0% | 75.0% | 0 | branches (true/false, switch cases, exceptions) |
| `com.expediagroup.baggageancillaryservice.service.pad.mapper.request.PadRequestMapper` | 100.0% | 60.0% | 60.0% | 0 | branches (true/false, switch cases, exceptions) + complex flows (nested logic, loops) |
| `com.expediagroup.baggageancillaryservice.service.srs.mapper.response.SrsResponseTypeMapper` | 98.1% | 63.6% | 61.1% | 1 | branches (true/false, switch cases, exceptions) + complex flows (nested logic, loops) |
| `com.expediagroup.baggageancillaryservice.service.pad.mapper.request.OriginDestinationFlightCriteriaMapper` | 100.0% | 66.7% | 60.0% | 0 | branches (true/false, switch cases, exceptions) + complex flows (nested logic, loops) |
| `com.expediagroup.baggageancillaryservice.util.ResponseMapperUtil` | 94.4% | 70.8% | 60.0% | 10 | complex flows (nested logic, loops) |

## 💡 Per-Class Recommendations

### `com.expediagroup.baggageancillaryservice.util.LoggingUtil`

- **Line coverage low**: add tests for public methods with business logic; include error paths and null checks. Cover util classes and constructors with logic.
- **Branch coverage low**: write tests for **both true/false** branches of `if`/`else`, each `case` in `switch`, and cover **boundary inputs** (0/1/many, empty collections, nulls). Include exception paths.
- **Complexity coverage low**: target **long or nested methods**. Add parameterized tests spanning edge cases; ensure loops test 0/1/many iterations; refactor huge methods if feasible to isolate branches.
- **Tactics**: use Mockito to isolate dependencies; verify interactions on clients/repositories; include timeouts/retries and negative tests; for controllers, add WebMvc/WebFlux tests; for services, add pure unit tests.

### `com.expediagroup.baggageancillaryservice.service.pad.mapper.request.SegmentFlightCriteriaMapper`

- **Branch coverage low**: write tests for **both true/false** branches of `if`/`else`, each `case` in `switch`, and cover **boundary inputs** (0/1/many, empty collections, nulls). Include exception paths.
- **Complexity coverage low**: target **long or nested methods**. Add parameterized tests spanning edge cases; ensure loops test 0/1/many iterations; refactor huge methods if feasible to isolate branches.
- **Tactics**: use Mockito to isolate dependencies; verify interactions on clients/repositories; include timeouts/retries and negative tests; for controllers, add WebMvc/WebFlux tests; for services, add pure unit tests.

### `com.expediagroup.baggageancillaryservice.service.pad.mapper.request.AncillaryCriteriaMapper`

- **Branch coverage low**: write tests for **both true/false** branches of `if`/`else`, each `case` in `switch`, and cover **boundary inputs** (0/1/many, empty collections, nulls). Include exception paths.
- **Tactics**: use Mockito to isolate dependencies; verify interactions on clients/repositories; include timeouts/retries and negative tests; for controllers, add WebMvc/WebFlux tests; for services, add pure unit tests.

### `com.expediagroup.baggageancillaryservice.service.pad.mapper.request.PadRequestMapper`

- **Branch coverage low**: write tests for **both true/false** branches of `if`/`else`, each `case` in `switch`, and cover **boundary inputs** (0/1/many, empty collections, nulls). Include exception paths.
- **Complexity coverage low**: target **long or nested methods**. Add parameterized tests spanning edge cases; ensure loops test 0/1/many iterations; refactor huge methods if feasible to isolate branches.
- **Tactics**: use Mockito to isolate dependencies; verify interactions on clients/repositories; include timeouts/retries and negative tests; for controllers, add WebMvc/WebFlux tests; for services, add pure unit tests.

### `com.expediagroup.baggageancillaryservice.service.srs.mapper.response.SrsResponseTypeMapper`

- **Branch coverage low**: write tests for **both true/false** branches of `if`/`else`, each `case` in `switch`, and cover **boundary inputs** (0/1/many, empty collections, nulls). Include exception paths.
- **Complexity coverage low**: target **long or nested methods**. Add parameterized tests spanning edge cases; ensure loops test 0/1/many iterations; refactor huge methods if feasible to isolate branches.
- **Tactics**: use Mockito to isolate dependencies; verify interactions on clients/repositories; include timeouts/retries and negative tests; for controllers, add WebMvc/WebFlux tests; for services, add pure unit tests.

### `com.expediagroup.baggageancillaryservice.service.pad.mapper.request.OriginDestinationFlightCriteriaMapper`

- **Branch coverage low**: write tests for **both true/false** branches of `if`/`else`, each `case` in `switch`, and cover **boundary inputs** (0/1/many, empty collections, nulls). Include exception paths.
- **Complexity coverage low**: target **long or nested methods**. Add parameterized tests spanning edge cases; ensure loops test 0/1/many iterations; refactor huge methods if feasible to isolate branches.
- **Tactics**: use Mockito to isolate dependencies; verify interactions on clients/repositories; include timeouts/retries and negative tests; for controllers, add WebMvc/WebFlux tests; for services, add pure unit tests.

### `com.expediagroup.baggageancillaryservice.util.ResponseMapperUtil`

- **Complexity coverage low**: target **long or nested methods**. Add parameterized tests spanning edge cases; ensure loops test 0/1/many iterations; refactor huge methods if feasible to isolate branches.
- **Tactics**: use Mockito to isolate dependencies; verify interactions on clients/repositories; include timeouts/retries and negative tests; for controllers, add WebMvc/WebFlux tests; for services, add pure unit tests.

---
_End of report_
