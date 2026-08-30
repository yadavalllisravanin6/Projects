-- Synthetic seed data ONLY - no real patient information.
-- Runs automatically on startup because of spring.jpa.defer-datasource-initialization=true

INSERT INTO intake_notes (patient_age, symptom_text, status, created_at) VALUES
(45, 'Chest tightness for 2 days, worse with exertion, mild shortness of breath', 'SUBMITTED', CURRENT_TIMESTAMP),
(29, 'Persistent dry cough for one week, no fever, mild fatigue', 'SUBMITTED', CURRENT_TIMESTAMP),
(62, 'Sudden severe chest pain radiating to left arm, sweating, nausea', 'SUBMITTED', CURRENT_TIMESTAMP),
(34, 'Lower back pain after lifting heavy furniture, no numbness or tingling', 'SUBMITTED', CURRENT_TIMESTAMP),
(8, 'High fever 102F for 2 days, sore throat, decreased appetite', 'SUBMITTED', CURRENT_TIMESTAMP),
(55, 'Blurred vision in right eye for 3 hours, mild headache', 'SUBMITTED', CURRENT_TIMESTAMP),
(41, 'Recurring migraines, 3 times this week, sensitivity to light', 'SUBMITTED', CURRENT_TIMESTAMP),
(19, 'Twisted ankle playing basketball, swelling, difficulty bearing weight', 'SUBMITTED', CURRENT_TIMESTAMP),
(70, 'Shortness of breath at rest, ankle swelling, fatigue over past week', 'SUBMITTED', CURRENT_TIMESTAMP),
(26, 'Skin rash on arms for 3 days, itchy, no known new products used', 'SUBMITTED', CURRENT_TIMESTAMP),
(50, 'Abdominal pain lower right side, worsening over 12 hours, mild fever', 'SUBMITTED', CURRENT_TIMESTAMP),
(33, 'Anxiety and heart palpitations during stressful periods at work', 'SUBMITTED', CURRENT_TIMESTAMP),
(66, 'Difficulty speaking and left-side weakness that started 30 minutes ago', 'SUBMITTED', CURRENT_TIMESTAMP),
(15, 'Mild acne breakout on face, requesting routine dermatology consult', 'SUBMITTED', CURRENT_TIMESTAMP),
(38, 'Seasonal allergies, sneezing and itchy eyes, wants prescription refill', 'SUBMITTED', CURRENT_TIMESTAMP);