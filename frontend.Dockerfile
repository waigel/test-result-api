FROM node:16-alpine AS deps
# Check https://github.com/nodejs/docker-node/tree/b4117f9333da4138b03a546ec926ef50a31506c3#nodealpine to understand why libc6-compat might be needed.
RUN apk add --no-cache libc6-compat
WORKDIR /app

# Install dependencies based on the preferred package manager
COPY package.json yarn.lock* package-lock.json* pnpm-lock.yaml* ./
RUN \
  if [ -f yarn.lock ]; then yarn --frozen-lockfile; \
  elif [ -f package-lock.json ]; then npm ci; \
  elif [ -f pnpm-lock.yaml ]; then yarn global add pnpm && pnpm i --frozen-lockfile; \
  else echo "Lockfile not found." && exit 1; \
  fi

FROM node:16-alpine AS builder
ARG TOLGEE_DOWNLOAD_API_KEY
RUN apk add --no-cache curl
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
ENV NEXT_TELEMETRY_DISABLED 1
ENV TOLGEE_DOWNLOAD_API_KEY=${TOLGEE_DOWNLOAD_API_KEY}
RUN ./scripts/i18n.sh
RUN ls i18n/
RUN yarn build

# If using npm comment out above and use below instead
# RUN npm run build

# Production image, copy all the files and run next
FROM node:16-alpine AS runner
MAINTAINER "Johannes Waigel"
WORKDIR /app

ENV NODE_ENV development

ARG GIT_BRANCH
ENV GIT_BRANCH=${GIT_BRANCH}

ARG GIT_COMMIT
ENV GIT_COMMIT=${GIT_COMMIT}

# Uncomment the following line in case you want to disable telemetry during runtime.
ENV NEXT_TELEMETRY_DISABLED 1

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

COPY --from=builder /app/public ./public
COPY --from=builder --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /app/.next/static ./.next/static
USER nextjs
EXPOSE 3000

ENV PORT 3000

LABEL BRANCH=${GIT_BRANCH}
LABEL COMMIT=${GIT_COMMIT}
LABEL PROJECT="testperfect"
LABEL COMPANY="novax-digital-gmbh"

CMD ["node", "server.js"]