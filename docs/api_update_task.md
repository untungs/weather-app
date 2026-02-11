# API Update Task: Replace OneCall API

The `data/2.5/onecall` API is no longer available for free. We need to replace it with two separate APIs:
- `/data/2.5/weather` for current weather.
- `/data/2.5/forecast` for daily weather (5-day forecast).

## Checklist
- [x] Define new API endpoints in `NetworkApi`
- [x] Implement new methods in `RetrofitNetwork` and `NetworkDataSource`
- [x] Update data models to match new API responses
- [x] Update `HomeViewModel` to fetch from both APIs
- [x] Update UI in `HomeScreen` to accommodate changes (remove day/night temps)
- [x] Verify functionality
